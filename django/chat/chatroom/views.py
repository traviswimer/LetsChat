from django.http import HttpResponse, Http404, HttpResponseRedirect
from django.shortcuts import render, get_object_or_404
from django.utils import simplejson
from django.db.models import Count

from chatroom.models import User, Message

from chatroom.forms import LoginForm, RegisterForm

from datetime import datetime, timedelta

import hashlib



def index(request):
    if request.session.get('user_id', False):
        return HttpResponseRedirect('/chatroom')
    else:
        error_msg = False

        if request.method == 'POST':
            form = LoginForm(request.POST)
            if form.is_valid():
                loginUsername = form.cleaned_data['username']
                loginPassword = hashlib.sha512( form.cleaned_data['password'] ).hexdigest()
                user_info = list( User.objects.filter(username=loginUsername, password=loginPassword) )
                if len(user_info) > 0:
                    request.session['user_id'] = user_info[0].id
                    return HttpResponseRedirect('/chatroom')
                else:
                    error_msg = "Incorrect Username/Password"
            else:
                error_msg = "Your username or password is invalid"
        else:
            form = LoginForm()

        context = {'form': form, 'error_msg': error_msg}
        return render(request, 'chatroom/index.html', context)


def chatroom(request):
    userid = request.session.get('user_id', False)
    if userid:
        username = User.objects.get(pk=userid).username
        context = {'userid': userid, 'username': username}
        return render(request, 'chatroom/chatroom.html', context)
    else:
        return HttpResponseRedirect('/')


def register(request):
    error_msg = False

    if request.method == 'POST':
        form = RegisterForm(request.POST)
        if form.is_valid():
            newUsername = form.cleaned_data['username']
            newPassword = hashlib.sha512( form.cleaned_data['password'] ).hexdigest()

            user_info = list(User.objects.filter(username=newUsername))
            if len(user_info) <= 0:
                newUser = User(username=newUsername, password=newPassword)
                newUser.save()
                request.session['user_id'] = newUser.pk
                return HttpResponseRedirect('/')
            else:
                error_msg = "Username already taken"
        else:
            error_msg = "Your username or password is invalid"
    else:
        form = RegisterForm()

    context = {'form': form, 'error_msg': error_msg}
    return render(request, 'chatroom/registerform.html', context)


def logout(request):
    request.session.flush()
    return HttpResponseRedirect('/')


def getMessages(request):
    if not request.is_ajax() or request.method != 'GET':
        raise Http404

    last_msg_id = request.GET['lastMsgId']

    messagesInfo = list(Message.objects.filter(pk__gt=last_msg_id))

    messages = []
    for message in messagesInfo:
        msgToAdd = {}
        msgToAdd['msgId'] = message.id
        msgToAdd['userid'] = message.user.id
        msgToAdd['user'] = message.user.username
        msgToAdd['text'] = message.message
        messages.append(msgToAdd)

    messages.reverse()
    return HttpResponse(simplejson.dumps({'messages': messages}), mimetype='application/json')


def getUsers(request):
    if not request.is_ajax() or request.method != 'GET':
        raise Http404

    anHourAgo = datetime.today() - timedelta(hours=1)

    usersInfo = list(Message.objects.filter(time__gt=anHourAgo).order_by('user'))

    users = []
    for message in usersInfo:
        if(len(users)>0):
            if users[-1] == message.user.id:
                continue
        users.append(message.user.id)


    return HttpResponse(simplejson.dumps({'users': users}), mimetype='application/json')


def postMessage(request):
    userid = request.session.get('user_id', False)
    if not request.is_ajax() or request.method != 'POST' or not userid:
        raise Http404

    try:
        postMsg = request.POST['msg']
        newMessage = Message(user=User.objects.get(pk=userid), message=postMsg, time=datetime.now())
        newMessage.save()
        success = True
    except:
        success = False

    return HttpResponse(simplejson.dumps({'success': success}), mimetype='application/json')

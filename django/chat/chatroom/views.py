from django.http import HttpResponse, Http404, HttpResponseRedirect
from django.shortcuts import render, get_object_or_404

from chatroom.models import User

from chatroom.forms import LoginForm, RegisterForm


def index(request):
    #user_authenticated = User.objects.get(pk=1)

    if request.session.get('user_id', False):
        return HttpResponseRedirect('/chatroom')
    else:
        error_msg = False

        if request.method == 'POST':
            form = LoginForm(request.POST)
            if form.is_valid():
                loginUsername = form.cleaned_data['username']
                loginPassword = form.cleaned_data['password']
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
        context = {'username': username}
        return render(request, 'chatroom/chatroom.html', context)
    else:
        return HttpResponseRedirect('/')


def register(request):
    error_msg = False

    if request.method == 'POST':
        form = RegisterForm(request.POST)
        if form.is_valid():
            newUsername = form.cleaned_data['username']
            newPassword = form.cleaned_data['password']

            user_info = list(User.objects.filter(username=newUsername))
            if len(user_info) <= 0:
                newUser = User(username=newUsername, password=newPassword)
                newUser.save()
                return HttpResponseRedirect('/')
            else:
                error_msg = "Username already taken"
        else:
            error_msg = "Your username or password is invalid"
    else:
        form = RegisterForm()

    context = {'form': form, 'error_msg': error_msg}
    return render(request, 'chatroom/registerform.html', context)

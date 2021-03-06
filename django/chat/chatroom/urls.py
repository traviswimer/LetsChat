from django.conf.urls import patterns, url

from chatroom import views

urlpatterns = patterns(
    '',
    url(r'^$', views.index, name='index'),
    url(r'^registerForm/', views.register, name='register'),
    url(r'^chatroom/$', views.chatroom, name='chatroom'),
    url(r'^chatroom/GetMessages', views.getMessages, name='getMessages'),
    url(r'^chatroom/GetUsers', views.getUsers, name='getUsers'),
    url(r'^chatroom/PostMessage', views.postMessage, name='postMessage'),
    url(r'^logout', views.logout, name='logout'),

    # ex: /polls/5/
    #url(r'^(?P<poll_id>\d+)/$', views.detail, name='detail'),
    # ex: /polls/5/results/
    #url(r'^(?P<poll_id>\d+)/results/$', views.results, name='results'),
    # ex: /polls/5/vote/
    #url(r'^(?P<poll_id>\d+)/vote/$', views.vote, name='vote'),
)

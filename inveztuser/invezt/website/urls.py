"""website URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/3.1/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from app import views

urlpatterns = [
    path('admin/', admin.site.urls),
    path('patterns_bought/<str:inveztid>/', views.patterns_bought_GET, name='patterns_bought_GET'),
    path('patterns_bought/', views.patterns_bought_POST, name='patterns_bought_POST'),
    path('patterns_list/', views.patterns_list, name='patterns_list'),
    path('pattern_articles/', views.pattern_articles, name='pattern_articles'),
    path('pattern_draw/', views.pattern_draw, name='pattern_draw'),
    path('add_user/', views.add_user, name='add_user'),
    path('create_stripe_key/<str:inveztid>/<str:api_version>/', views.create_stripe_key, name='create_stripe_key'),
    path('create_stripe_paymentIntent/', views.create_stripe_paymentIntent, name='create_stripe_paymentIntent'),
    path('confirm_payment/', views.confirm_payment, name='confirm_payment'),
    path('add_to_cart/', views.add_to_cart, name='add_to_cart'),
    path('get_cart/<str:inveztid>/', views.get_cart, name='get_cart'),
    path('remove_from_cart/', views.remove_from_cart, name='remove_from_cart'),
]


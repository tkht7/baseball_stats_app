import django_filters
from rest_framework import viewsets

from .models import Result
from .serializer import ResultSerializer

from django.shortcuts import render


class ResultViewSet(viewsets.ModelViewSet):
    queryset = Result.objects.all()
    serializer_class = ResultSerializer
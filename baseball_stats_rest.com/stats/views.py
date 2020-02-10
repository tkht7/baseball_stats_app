import django_filters
from rest_framework import viewsets

from .models import Batting, Pitching
from .serializer import BattingSerializer, PitchingSerializer

from django.shortcuts import render


class BattingViewSet(viewsets.ModelViewSet):
    queryset = Batting.objects.all()
    serializer_class = BattingSerializer


class PitchingViewSet(viewsets.ModelViewSet):
    queryset = Pitching.objects.all()
    serializer_class = PitchingSerializer

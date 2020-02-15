import django_filters
from rest_framework import viewsets, generics

from .models import Batting, Pitching, Fielding
from .serializer import BattingSerializer, PitchingSerializer, FieldingSerializer

from django.shortcuts import render


class BattingViewSet(viewsets.ModelViewSet):
    queryset = Batting.objects.all()
    serializer_class = BattingSerializer
    filter_fields = ('team',)


class PitchingViewSet(viewsets.ModelViewSet):
    queryset = Pitching.objects.all()
    serializer_class = PitchingSerializer
    filter_fields = ('team',)


class FieldingViewSet(viewsets.ModelViewSet):
    queryset = Fielding.objects.all()
    serializer_class = FieldingSerializer
    filter_fields = ('team',)

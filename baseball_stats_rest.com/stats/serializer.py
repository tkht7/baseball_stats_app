from rest_framework import serializers
from .models import Batting, Pitching

class BattingSerializer(serializers.ModelSerializer):
    class Meta:
        model = Batting
        fields = '__all__'

class PitchingSerializer(serializers.ModelSerializer):
    class Meta:
        model = Pitching
        fields = '__all__'

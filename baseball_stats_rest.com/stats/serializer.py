from rest_framework import serializers
from .models import Result

class ResultSerializer(serializers.ModelSerializer):
    class Meta:
        model = Result
        fields = ('team',
                'dominant',
                'name',
                'b_num',
                'point',
                'hit',
                'hit2',
                'hit3',
                'homerun',
                'b_point',
                'steal',
                'steal_out',
                's_hit',
                's_fly',
                'fourball',
                'intent_four',
                'deadball',
                's_out',
                'double_out')
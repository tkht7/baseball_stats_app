from rest_framework import viewsets
from django_filters import rest_framework as filters

from .models import Batting, Pitching, Fielding
from .serializer import BattingSerializer, PitchingSerializer, FieldingSerializer


# Batting用のフィルタセット
class BattingFilter(filters.FilterSet):
    # フィルタの定義
    name = filters.CharFilter(field_name='name', lookup_expr='exact')
    team = filters.CharFilter(field_name='team', lookup_expr='exact')
    game_r = filters.RangeFilter(field_name='games')
    handed = filters.CharFilter(field_name='handed', lookup_expr='exact')
    pa_r = filters.RangeFilter(field_name='plate_appearances')
    at_bats_r = filters.RangeFilter(field_name='at_bats')

    order_by = filters.OrderingFilter(
        fields =(
            ('games', 'game'),
            ('plate_appearances', 'pa'),
            ('at_bats', 'ab'),
            ('runs', 'run'),
            ('hits', 'h'),
            ('hits2', 'h2'),
            ('hits3', 'h3'),
            ('homeruns', 'h4'),
            ('runs_batted_in', 'rbi'),
            ('steals', 'steal'),
            ('caught_steals', 'c_steal'),
            ('sacrifice_hits', 'sac_hits'),
            ('sacrifice_flies', 'sac_flies'),
            ('bases_on_balls', 'bb'),
            ('intentional_walks', 'intent_bb'),
            ('hits_by_pitch', 'hbp'),
            ('strike_outs', 'so'),
            ('double_plays', 'dp'),
            ('batting_average', 'ba'),
            ('on_base_percentage', 'obp'),
            ('slugging_perventage', 'slg'),
            ('ops', 'OPS'),
            ('stolen_bases_percentage', 'sbp'),
        )
    )
    

    class Meta:
        model = Batting
        fields = ['name', 'team', 'game_r', 'handed', 'pa_r', 'at_bats_r', 'order_by']


# Pitching用のフィルタセット
class PitchingFilter(filters.FilterSet):
    # フィルタの定義
    team = filters.CharFilter(field_name='team', lookup_expr='exact')
    name = filters.CharFilter(field_name='name', lookup_expr='exact')
    handed = filters.CharFilter(field_name='handed', lookup_expr='exact')
    games_r = filters.RangeFilter(field_name='games')
    innings_r = filters.RangeFilter(field_name='innings')

    order_by = filters.OrderingFilter(
        fields=(
            ('games', 'game'),
            ('earned_run_average_f', 'era'),
            ('wins', 'win'),
            ('loses', 'lose'),
            ('saves', 'save'),
            ('holds', 'hold'),
            ('completes', 'comp'),
            ('shut_out_wins', 'sow'),
            ('no_BB_completes', 'nbc'),
            ('winning_percentage', 'wp'),
            ('batters_faced', 'bf'),
            ('outs', 'out'),
            ('innings', 'inn'),
            ('hits', 'hit'),
            ('homeruns', 'hr'),
            ('bases_on_balls', 'bb'),
            ('intentional_walks', 'intent_bb'),
            ('hits_by_pitch', 'hbp'),
            ('strike_outs', 'so'),
            ('wild_pitches', 'wp'),
            ('balks', 'balk'),
            ('runs', 'run'),
            ('earned_runs', 'er'),
        )
    )

    class Meta:
        model = Pitching
        fields = ['team', 'name', 'handed', 'games_r', 'innings_r', 'order_by']


class BattingViewSet(viewsets.ModelViewSet):
    queryset = Batting.objects.all()
    serializer_class = BattingSerializer
    filter_class = BattingFilter


class PitchingViewSet(viewsets.ModelViewSet):
    queryset = Pitching.objects.all()
    serializer_class = PitchingSerializer
    filter_class = PitchingFilter


class FieldingViewSet(viewsets.ModelViewSet):
    queryset = Fielding.objects.all()
    serializer_class = FieldingSerializer
    filter_fields = ('team', 'name')


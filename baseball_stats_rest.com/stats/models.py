from django.db import models
import requests
from bs4 import BeautifulSoup
import time


team_choice = (
    ('L', '埼玉西武ライオンズ'),
    ('H', '福岡ソフトバンクホークス'),
    ('E', '東北楽天ゴールデンイーグルス'),
    ('M', '千葉ロッテマリーンズ'),
    ('F', '北海道日本ハムファイターズ'),
    ('B', 'オリックス・バファローズ'),
    ('G', '読売ジャイアンツ'),
    ('DB', '横浜DeNAベイスターズ'),
    ('T', '阪神タイガース'),
    ('C', '広島東洋カープ'),
    ('D', '中日ドラゴンズ'),
    ('S', '東京ヤクルトスワローズ'),
)

# 打者成績
class Batting(models.Model):
    dominant_choice = (
        ('', '右'),
        ('*', '左'),
        ('+', '両')
    )
    
    name = models.CharField(max_length=255, verbose_name='名前')
    games = models.IntegerField(verbose_name='試合数')
    team = models.CharField(max_length=255, verbose_name='チーム', choices=team_choice)
    handed = models.CharField(max_length=2, verbose_name='席', choices=dominant_choice)
    plate_appearances = models.IntegerField(verbose_name='打席')
    at_bats = models.IntegerField(verbose_name='打数') # 追加
    runs = models.IntegerField(verbose_name='得点')    
    hits = models.IntegerField(verbose_name='安打')
    hits2 = models.IntegerField(verbose_name='二塁打')
    hits3 = models.IntegerField(verbose_name='三塁打')
    homeruns = models.IntegerField(verbose_name='本塁打')
    runs_batted_in = models.IntegerField(verbose_name='打点')
    steals = models.IntegerField(verbose_name='盗塁')
    caught_steals = models.IntegerField(verbose_name='盗塁死')
    sacrifice_hits = models.IntegerField(verbose_name='犠打')
    sacrifice_flies = models.IntegerField(verbose_name='犠飛')
    bases_on_balls = models.IntegerField(verbose_name='四球')
    intentional_walks = models.IntegerField(verbose_name='故意四')
    hits_by_pitch = models.IntegerField(verbose_name='死球')
    strike_outs = models.IntegerField(verbose_name='三振')
    double_plays = models.IntegerField(verbose_name='併殺打')
    # batting_average = models.FloatField(verbose_name='打率')
    batting_average = models.CharField(max_length=255, verbose_name='打率')
    # on_base_percentage = models.FloatField(verbose_name='出塁率')
    on_base_percentage = models.CharField(max_length=255, verbose_name='出塁率')
    # slugging_percentage = models.FloatField(verbose_name='長打率')
    slugging_percentage = models.CharField(max_length=255, verbose_name='長打率')
    # ops = models.FloatField(verbose_name='OPS')
    ops = models.CharField(max_length=255, verbose_name='OPS')
    # stolen_bases_percentage = models.FloatField(verbose_name='盗塁成功率')
    stolen_bases_percentage = models.CharField(max_length=255, verbose_name='盗塁成功率')
    year = models.IntegerField(verbose_name='年度')

    
    def __str__(self):
        return self.name


# 投手成績
class Pitching(models.Model):
    dominant_choice = (
        ('', '右'),
        ('*', '左'),
    )

    team = models.CharField(max_length=255, verbose_name='チーム', choices=team_choice)
    handed = models.CharField(max_length=2, verbose_name='投', choices=dominant_choice)
    name = models.CharField(max_length=255, verbose_name='名前')
    games = models.IntegerField(verbose_name='登板')
    earned_run_average_f = models.FloatField(verbose_name='防御率_f')
    earned_run_average = models.CharField(max_length=255, verbose_name='防御率_c')
    wins = models.IntegerField(verbose_name='勝利')
    loses = models.IntegerField(verbose_name='敗戦')
    saves = models.IntegerField(verbose_name='セーブ')
    holds = models.IntegerField(verbose_name='ホールド')
    completes = models.IntegerField(verbose_name='完投')
    shut_out_wins = models.IntegerField(verbose_name='完封')
    no_BB_completes = models.IntegerField(verbose_name='無四球')
    # winning_percentage = models.FloatField(verbose_name='勝率')
    winning_percentage = models.CharField(max_length=255, verbose_name='勝率')
    batters_faced = models.IntegerField(verbose_name='対戦打者')
    outs = models.IntegerField(verbose_name='獲得アウト')
    innings = models.FloatField(verbose_name='イニング')
    hits = models.IntegerField(verbose_name='被安打')
    homeruns = models.IntegerField(verbose_name='被本塁打')
    bases_on_balls = models.IntegerField(verbose_name='与四球')
    intentional_walks = models.IntegerField(verbose_name='与故意四')
    hits_by_pitch = models.IntegerField(verbose_name='与死球')
    strike_outs = models.IntegerField(verbose_name='奪三振')
    wild_pitches = models.IntegerField(verbose_name='暴投')
    balks = models.IntegerField(verbose_name='ボーク')
    runs = models.IntegerField(verbose_name='失点')
    earned_runs = models.IntegerField(verbose_name='自責点')
    year = models.IntegerField(verbose_name='年度')

    def __str__(self):
        return self.name
    

# 守備成績
class Fielding(models.Model):
    dominant_choice = (
    ('', '右'),
    ('*', '左'),
    )

    team = models.CharField(max_length=255, verbose_name='チーム', choices=team_choice)
    name = models.CharField(max_length=255, verbose_name='名前')
    handed = models.CharField(max_length=2, verbose_name='投', choices=dominant_choice)
    position = models.CharField(max_length=255, verbose_name='守備位置')
    games = models.IntegerField(verbose_name='試合')
    put_outs = models.IntegerField(verbose_name='刺殺')
    assists = models.IntegerField(verbose_name='補殺')
    errors = models.IntegerField(verbose_name='失策')
    double_plays = models.IntegerField(verbose_name='併殺')
    passed_balls = models.IntegerField(verbose_name='捕逸', null=True, default=0)
    # fielding_average = models.FloatField(verbose_name='守備率')
    fielding_average = models.CharField(max_length=255, verbose_name='守備率')
    year = models.IntegerField(verbose_name='年度')

    def __str__(self):
        return self.name


# ここに書かないとcircular importになる
from .create_database import create_Batting_Database, create_Pitching_Database, create_Fielding_Database


'''
以下は、makemigrations、migrateをしてから
コメントアウトを外し、再度migrateを行った後に
コメントアウトをし直してrunserverを行う
'''
# スクレイピング実行(打者)(投手)(守備)
# create_Batting_Database()
# create_Pitching_Database()
# create_Fielding_Database()


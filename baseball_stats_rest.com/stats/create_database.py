from .models import Batting, Pitching

from django.db import models
import requests
from bs4 import BeautifulSoup
import time

# スクレイピング（Batting）
def Create_Batting_Database():
    team_list = ['l','h','f','b','m','e','c','s','g','db','d','t']
    for team_token in team_list:
        URL = 'http://npb.jp/bis/2019/stats/idb1_{}.html'.format(team_token)
        res = requests.get(URL)
        res.encoding = res.apparent_encoding
        soup = BeautifulSoup(res.content, 'html.parser') # BeautifulSoupの初期化
        tags = soup.find_all('tr', class_='ststats')

        batting_obj = []
        for tag in tags:
            result = []
            data = tag.find_all('td')
            # 文字列にする
            for i in range(len(data)):
                if not(i == 4 or i == 10 or i == 21 or i == 22 or i == 23):
                    result.append(data[i].text.replace('\u3000', ' '))

            if result[0] == '*':
                result[0] = '左'
            elif result[0] == '+':
                result[0] = '両'
            else:
                result[0] = '右'
            
            team_token = team_token.upper()

            # print(result)
            b = Batting(team = team_token,
                        handed = result[0],
                        name = result[1],
                        games = result[2],
                        plate_appearances = result[3],
                        runs = result[4],
                        hits = result[5],
                        hits2 = result[6],
                        hits3 = result[7],
                        homeruns = result[8],
                        runs_batted_in = result[9],
                        steals = result[10],
                        caught_steals = result[11],
                        sacrifice_hits = result[12],
                        sacrifice_flies = result[13],
                        bases_on_balls = result[14],
                        intentional_walks = result[15],
                        hits_by_pitch = result[16],
                        strike_outs = result[17],
                        double_plays = result[18],
            )
            batting_obj.append(b)

        Batting.objects.bulk_create(batting_obj)

        time.sleep(1)


# スクレイピング（Pitching）
def Create_Pitching_Database():
    team_list = ['l','h','f','b','m','e','c','s','g','db','d','t']
    for team_token in team_list:
        URL = 'http://npb.jp/bis/2019/stats/idp1_{}.html'.format(team_token)
        res = requests.get(URL)
        res.encoding = res.apparent_encoding
        soup = BeautifulSoup(res.content, 'html.parser') # BeautifulSoupの初期化
        tags = soup.find_all('tr', class_='ststats')

        pitching_obj = []
        for tag in tags:
            result = []
            data = tag.find_all('td')
            # 文字列にする
            for i in range(len(data)):
                if not(i == 11 or i == 25):
                    result.append(data[i].text.replace('\u3000', ' '))
            
            if result[0] == '*':
                result[0] = '左'
            else:
                result[0] = '右'

            if result[12] == '+':
                result[12] = '0'

            # イニング数を獲得アウト数に変換
            A = result[12]
            f = result[13]
            
            R = int(A) * 3 + float('0' + f) * 10
            result[12] = str(int(R))

            team_token = team_token.upper()

            # print(result)
            b = Pitching(team = team_token,
                        handed = result[0],
                        name = result[1],
                        games = result[2],
                        wins = result[3],
                        loses = result[4],
                        saves = result[5],
                        holds = result[6],
                        #hps = result[7],
                        completes = result[8],
                        shut_out_wins = result[9],
                        no_BB_completes = result[10],
                        batters_faced = result[11],
                        outs = result[12],
                        hits = result[14],
                        homeruns = result[15],
                        bases_on_balls = result[16],
                        intentional_walks = result[17],
                        hits_by_pitch = result[18],
                        strike_outs = result[19],
                        wild_pitches = result[20],
                        balks = result[21],
                        runs = result[22],
                        earned_runs = result[23],
            )
            pitching_obj.append(b)

        Pitching.objects.bulk_create(pitching_obj)

        time.sleep(1)
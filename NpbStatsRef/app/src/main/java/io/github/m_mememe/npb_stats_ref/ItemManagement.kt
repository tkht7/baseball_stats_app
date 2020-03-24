package io.github.m_mememe.npb_stats_ref

class ItemManagement {
    val leagueLogo = listOf(
        // パ・リーグ
        R.drawable.lions,
        R.drawable.hawks,
        R.drawable.eagles,
        R.drawable.marines,
        R.drawable.fighters,
        R.drawable.buffaloes,
        // セ・リーグ
        R.drawable.giants,
        R.drawable.baystars,
        R.drawable.tigers,
        R.drawable.carp,
        R.drawable.dragons,
        R.drawable.swallows
    )

    val leagueId = listOf(
        // パ・リーグ
        R.string.lions,
        R.string.hawks,
        R.string.eagles,
        R.string.marines,
        R.string.fighters,
        R.string.buffaloes,
        // セ・リーグ
        R.string.giants,
        R.string.baystars,
        R.string.tigers,
        R.string.carp,
        R.string.dragons,
        R.string.swallows
    )

    val teamList = listOf<String>(
        "L",        //西武ライオンズ
        "H",        //ソフトバンク
        "E",       //楽天
        "M",      //千葉ロッテ
        "F",     //日本ハム
        "B",    //オリックス
        "G",       //ジャイアンツ
        "DB",     //DeNA
        "T",       //阪神
        "C",         //カープ
        "D",      //中日
        "S"      //ヤクルト
    )

    val teamList2 = listOf<String>(
        "西武",
        "ソフトバンク",
        "楽天",
        "千葉ロッテ",
        "日本ハム",
        "オリックス",
        "巨人",
        "DeNA",
        "阪神",
        "カープ",
        "中日",
        "ヤクルト"
    )

    val rankingTeamList = listOf<String>(
        "L",        //西武ライオンズ
        "H",        //ソフトバンク
        "E",       //楽天
        "M",      //千葉ロッテ
        "F",     //日本ハム
        "B",    //オリックス
        "G",       //ジャイアンツ
        "DB",     //DeNA
        "T",       //阪神
        "C",         //カープ
        "D",      //中日
        "S",      //ヤクルト
        "all"
    )

    val rankingTeamList2 = listOf<String>(
        "西武",
        "ソフトバンク",
        "楽天",
        "千葉ロッテ",
        "日本ハム",
        "オリックス",
        "巨人",
        "DeNA",
        "阪神",
        "カープ",
        "中日",
        "ヤクルト",
        "全体"
    )

    val statsTypeList = listOf(
        "batting",
        "pitching",
        "fielding"
    )
    val statsTypeList2 = listOf(
        "打撃成績",
        "投手成績",
        "守備成績"
    )

    val statsTypeList3 = listOf(
        "batting",
        "pitching"
    )
    val statsTypeList4 = listOf(
        "打撃成績",
        "投手成績"
    )

    val battingItemList = listOf(
        "games",
        "handed",
        "batting_average",
        "plate_appearances",
        "at_bats",
        "runs",
        "hits",
        "hits2",
        "hits3",
        "homeruns",
        "runs_batted_in",
        "strike_outs",
        "bases_on_balls",
        "intentional_walks",
        "sacrifice_hits",
        "sacrifice_flies",
        "steals",
        "caught_steals",
        "stolen_bases_percentage",
        "double_plays",
        "on_base_percentage",
        "slugging_percentage",
        "ops"
    )
    val pitchingItemList = listOf(
        "games",
        "handed",
        "earned_run_average",
        "wins",
        "loses",
        "holds",
        "saves",
        "completes",
        "shut_out_wins",
        "winning_percentage",
        "innings",
        "batters_faced",
        "hits",
        "homeruns",
        "strike_outs",
        "bases_on_balls",
        "intentional_walks",
        "hits_by_pitch",
        "wild_pitches",
        "balks",
        "runs",
        "earned_runs"
    )
    val fieldingItemList = listOf(
        "games",
        "put_outs",
        "assists",
        "errors",
        "double_plays",
        "passed_balls",
        "fielding_average"
    )
    val battingItemList2 = listOf(
        "試合数",
        "左右",
        "打率",
        "打席",
        "打数",
        "得点",
        "安打",
        "二塁打",
        "三塁打",
        "本塁打",
        "打点",
        "三振",
        "四球",
        "敬遠",
        "犠打",
        "犠飛",
        "盗塁",
        "盗塁死",
        "盗塁\n成功率",
        "併殺打",
        "出塁率",
        "長打率",
        "OPS"
    )
    val pitchingItemList2 = listOf(
        "試合数",
        "左右",
        "防御率",
        "勝利",
        "敗戦",
        "ホールド",
        "セーブ",
        "完投",
        "完封",
        "勝率",
        "イニング",
        "対戦\n打者数",
        "被安打",
        "被本塁打",
        "奪三振",
        "与四球",
        "故意四球",
        "与死球",
        "暴投",
        "ボーク",
        "失点",
        "自責点"
    )
    val fieldingItemList2 = listOf(
        "試合数",
        "刺殺",
        "補殺",
        "失策",
        "併殺",
        "捕逸",
        "守備率"
    )

    // ランキングに入らないものを削る
    val battingRankingList = listOf(
        "games",
        "batting_average",
        "plate_appearances",
        "at_bats",
        "runs",
        "hits",
        "hits2",
        "hits3",
        "homeruns",
        "runs_batted_in",
        "strike_outs",
        "bases_on_balls",
        "intentional_walks",
        "sacrifice_hits",
        "sacrifice_flies",
        "steals",
        "caught_steals",
        "stolen_bases_percentage",
        "double_plays",
        "on_base_percentage",
        "slugging_percentage",
        "ops"
    )
    val pitchingRankingList = listOf(
        "games",
        "earned_run_average",
        "wins",
        "loses",
        "holds",
        "saves",
        "completes",
        "shut_out_wins",
        "winning_percentage",
        "innings",
        "batters_faced",
        "hits",
        "homeruns",
        "strike_outs",
        "bases_on_balls",
        "intentional_walks",
        "hits_by_pitch",
        "wild_pitches",
        "balks",
        "runs",
        "earned_runs"
    )

    val battingRankingList2 = listOf(
        "試合数",
        "打率",
        "打席",
        "打数",
        "得点",
        "安打",
        "二塁打",
        "三塁打",
        "本塁打",
        "打点",
        "三振",
        "四球",
        "敬遠",
        "犠打",
        "犠飛",
        "盗塁",
        "盗塁死",
        "盗塁\n成功率",
        "併殺打",
        "出塁率",
        "長打率",
        "OPS"
    )
    val pitchingRankingList2 = listOf(
        "試合数",
        "防御率",
        "勝利",
        "敗戦",
        "ホールド",
        "セーブ",
        "完投",
        "完封",
        "勝率",
        "イニング",
        "対戦\n打者数",
        "被安打",
        "被本塁打",
        "奪三振",
        "与四球",
        "故意四球",
        "与死球",
        "暴投",
        "ボーク",
        "失点",
        "自責点"
    )

    // URLのorder_byで指定する部分
    val battingRankingListURL = listOf(
        "game",
        "ba",
        "pa",
        "ab",
        "run",
        "h",
        "h2",
        "h3",
        "h4",
        "rbi",
        "so",
        "bb",
        "int_bb",
        "hbp",
        "sac_hits",
        "sac_flies",
        "steal",
        "c_steal",
        "sbp",
        "dp",
        "obp",
        "slg",
        "OPS"
    )
    val pitchingRankingListURL = listOf(
        "game",
        "era",
        "win",
        "lose",
        "hold",
        "save",
        "comp",
        "sow",
        "winp",
        "inn",
        "bf",
        "hit",
        "hr",
        "so",
        "bb",
        "int_bb",
        "hbp",
        "wildp",
        "balk",
        "run",
        "er"
    )
}
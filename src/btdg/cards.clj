(ns btdg.cards)

(def roles
  {:sheriff  (repeat 1 {:role :sheriff})
   :deputy   (repeat 2 {:role :deputy})
   :outlaw   (repeat 3 {:role :outlaw})
   :renegade (repeat 2 {:role :renegade})})

(def characters
  [{:name "Bart Cassidy"
    :life 8
    :ability "You may take an arrow instead of losing a life point (except to Indians or Dynamite). You cannot use this ability if you lose a life point to Indian or Dynamite, only for 1, 2, or Gatling Gun. You may not use this ability to take the last arrow remaining in the pile."}
   {:name "Black Jack"
    :life 8
    :ability "You may re-roll Dynamite (not if you roll three or more!). If you roll three or more Dynamite at once (or in total if you didn't re-roll them), follow the usual rules (your turn ends, etc.)."}
   {:name "Calamity Jane"
    :life 8
    :ability "You can use 1 as 2 and vice-versa."}
   {:name "El Gringo"
    :life 7
    :ability "When a player makes you lose one or more life points, he must take an arrow. Life points lost to Indians or Dynamite are not affected."}
   {:name "Jesse Jones"
    :life 9
    :ability "If you have four life points or less, you gain two if you use Beer for yourself. For example, if you have four life points and use two beers, you get four life points."}
   {:name "Jourdonnais"
    :life 7
    :ability "You never lose more than one life point to Indians."}
   {:name "Kit Carlson"
    :life 7
    :ability "For each Gatling Gun you may discard one arrow from any player. You may choose to discard your own arrows. If you roll three Gatling Gun, you discard all your own arrows, plus three from any player(s) (of course, you still deal one damage to each other player)."}
   {:name "Lucky Duke"
    :life 8
    :ability "You may make one extra re-roll. You may roll the dice a total of four times on your turn."}
   {:name "Paul Regret"
    :life 9
    :ability "You never lose life points to the Gatling Gun."}
   {:name "Pedro Ramirez"
    :life 8
    :ability "Each time you lose a life point, you may discard one of your arrows. You still lose the life point when you use this ability."}
   {:name "Rose Doolan"
    :life 9
    :ability "You may use 1 or 2 for players sitting one place further. With 1 you may hit a player sitting up to two places away, and with 2 you may hit a player sitting two or three places away."}
   {:name "Sid Ketchum"
    :life 8
    :ability "At the beginning of your turn, any player of your choice gains one life point. You may also choose yourself."}
   {:name "Slab the Killer"
    :life 8
    :ability "Once per turn, you can use a Beer to double a 1 or 2. The dice you double takes two life points away from the same player (you can't choose two different players). The Beer in this case does not provide any life points."}
   {:name "Suzy Lafayette"
    :life 8
    :ability "If you didn't roll any 1 or 2, you gain two life points. This only applies at the end of your turn, not during your re-rolls."}
   {:name "Vulture Sam"
    :life 9
    :ability "Each time another player is eliminated, you gain two life points."}
   {:name "Willy the Kid"
    :life 8
    :ability "You only need 2 Gatling Gun results to use the Gatling Gun. You can activate the Gatling Gun only once per turn, even if you roll more than two Gatling Gun results."}])

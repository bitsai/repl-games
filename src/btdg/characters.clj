(ns btdg.characters)

(def base
  [{:name "BART CASSIDY"
    :max-life 8
    :ability "You may take an arrow instead of losing a life point (except to Indians or Dynamite)."}
   {:name "BLACK JACK"
    :max-life 8
    :ability "You may re-roll DYNAMITE (not if you roll three or more!)."}
   {:name "CALAMITY JANE"
    :max-life 8
    :ability "You can use 1 as 2 and vice-versa."}
   {:name "EL GRINGO"
    :max-life 7
    :ability "When a player makes you lose one or more life points, he must take an arrow."}
   {:name "JESSE JONES"
    :max-life 9
    :ability "If you have four life points or less, you gain two if you use BEER for yourself."}
   {:name "JOURDONNAIS"
    :max-life 7
    :ability "You never lose more than one life point to Indians."}
   {:name "KIT CARLSON"
    :max-life 7
    :ability "For each GATLING GUN you may discard one arrow from any player."}
   {:name "LUCKY DUKE"
    :max-life 8
    :ability "You may make one extra re-roll."}
   {:name "PAUL REGRET"
    :max-life 9
    :ability "You never lose life points to the Gatling Gun."}
   {:name "PEDRO RAMIREZ"
    :max-life 8
    :ability "Each time you lose a life point, you may discard one of your arrows."}
   {:name "ROSE DOOLAN"
    :max-life 9
    :ability "You may use 1 or 2 for players sitting one place further."}
   {:name "SID KETCHUM"
    :max-life 8
    :ability "At the beginning of your turn, any player of your choice gains one life point."}
   {:name "SLAB THE KILLER"
    :max-life 8
    :ability "Once per turn, you can use a BEER to double a 1 or 2."}
   {:name "SUZY LAFAYETTE"
    :max-life 8
    :ability "If you didn't roll any 1 or 2, you gain two life points."}
   {:name "VULTURE SAM"
    :max-life 9
    :ability "Each time another player is eliminated, you gain two life points."}
   {:name "WILLY THE KID"
    :max-life 8
    :ability "You only need two GATLING GUN to use the Gatling Gun."}])

(def old-saloon
  [{:name "JOSE DELGADO"
    :max-life 7
    :ability "You may use the Loudmouth die without replacing a base die (roll six dice total)."}
   {:name "TEQUILA JOE"
    :max-life 7
    :ability "You may use the Coward die without replacing a base die (roll six dice total)."}
   {:name "APACHE KID"
    :max-life 9
    :ability "If you roll ARROW, you may take the Indian Chief's Arrow from another player."}
   {:name "BILL NOFACE"
    :max-life 9
    :ability "Apply ARROW results only after your last roll."}
   {:name "ELENA FUENTE"
    :max-life 7
    :ability "Each time you roll one or more ARROW, you may give one of those arrows to a player of your choice."}
   {:name "VERA CUSTER"
    :max-life 7
    :ability "Each time you must lose life points for 1 or 2, you lose 1 life point less."}
   {:name "DOC HOLYDAY"
    :max-life 8
    :ability "Each time you use three or more 1 and/or 2, you also regain 2 life points."}
   {:name "MOLLY STARK"
    :max-life 8
    :ability "Each time another player must lose 1 or more life points, you can lose them in his place."}])

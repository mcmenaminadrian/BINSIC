8 PRINT "You are a pilot in a Second World War bomber."
10 PRINT "Which side -- Italy(1), Allies(2), Japan(3), Germany(4)"
12 INPUT A
20 IF A > 0 AND A < 5 THEN GOTO 25
22 PRINT "TRY AGAIN..."
24 GOTO 10
25 IF A = 1 THEN GOTO 30
26 IF A = 2 THEN GOTO 110
27 IF A = 3 THEN GOTO 200
28 IF A = 4 THEN GOTO 220
30 PRINT "YOUR TARGET -- Albania(1), Greece(2), North Africa(3)"
35 INPUT B
40 IF B>0 AND B<4 THEN GOTO 45
42 PRINT "TRY AGAIN..."
43 GOTO 30
45 PRINT ""
46 IF B = 1 THEN GOTO 50
47 IF B = 2 THEN GOTO 80
48 IF B = 3 THEN GOTO 90
50 PRINT "SHOULD BE EASY -- YOU'RE FLYING A NAZI-MADE PLANE."
60 GOTO 280
80 PRINT "BE CAREFUL!!!"
85 GOTO 280
90 PRINT "Going for the oil, eh?"
95 GOTO 280
110 PRINT "AIRCRAFT -- Liberator(1), B-29(2), B-17(3), Lancaster(4)"
115 INPUT G
120 IF G>0 AND G<5 THEN GOTO 125
122 PRINT "TRY AGAIN..."
123 GOTO 110
125 PRINT ""
126 IF G = 1 THEN GOTO 130
127 IF G = 2 THEN GOTO 150
128 IF G = 3 THEN GOTO 170
129 IF G = 4 THEN GOTO 190
130 PRINT "You have got 2 tons of bombs flying for Ploesti."
135 GOTO 280
150 PRINT "You are dumping the A-bomb on Hiroshima."
155 GOTO 280
170 PRINT "You are chasing the Bismark in the North Atlantic."
175 GOTO 280
190 PRINT "You are targeting the Ruhr."
195 GOTO 280
200 PRINT
201 PRINT "You are flying a KAMIKAZE mission over the USS Lexington."
205 PRINT "Your first Kamikaze mission? (Y OR N)"
206 INPUT F$
207 IF F$ = "N" THEN LET S = 0
208 IF F$ = "N" THEN GOTO 358
210 PRINT ""
212 IF RND > 0.65 THEN GOTO 325
215 GOTO 380
220 PRINT "A NAZI, EH?  Oh well.  Are you going for Russia(1),"
230 PRINT "England(2), or France(3)"
231 INPUT M
232 IF M>0 AND M<4 THEN GOTO 235
233 PRINT "TRY AGAIN..." 
234 GOTO 220
235 PRINT ""
240 IF M = 1 THEN GOTO 250
242 IF M = 2 THEN GOTO 260
243 IF M = 3 THEN GOTO 270
250 PRINT "YOU'RE NEARING STALINGRAD."
255 GOTO 280
260 PRINT "NEARING LONDON.  BE CAREFUL, THEY'VE GOT RADAR."
265 GOTO 280
270 PRINT "NEARING VERSAILLES.  DUCK SOUP.  THEY'RE NEARLY DEFENSELESS."
280 PRINT
285 PRINT "HOW MANY MISSIONS HAVE YOU FLOWN"
287 INPUT D
290 IF D < 160 THEN GOTO 300
292 PRINT "MISSIONS, NOT MILES..." 
295 PRINT "150 missions is high even for old-timers."
297 PRINT "NOW THEN, ";
298 GOTO 285
300 PRINT
302 IF D < 100 THEN GOTO 310
305 PRINT "THAT'S PUSHING THE ODDS!"
307 GOTO 320
310 IF D < 25 THEN PRINT "FRESH OUT OF TRAINING, EH?"
320 PRINT
322 IF D < 160 * RND THEN GOTO 330
325 PRINT "DIRECT HIT!!!! ", INT(100 * RND), " KILLED."
327 PRINT "MISSION SUCCESSFUL."
328 GOTO 390
330 PRINT "MISSED TARGET BY ", INT(2+30 * RND), " MILES!"
335 PRINT "Now you are REALLY in for it !!"
336 PRINT
340 PRINT "Does the enemy have GUNS(1), MISSILES(2), or BOTH(3)"
342 INPUT R
345 IF R > 0 AND R < 4 THEN GOTO 350
347 PRINT "TRY AGAIN..."
348 GOTO 340
350 PRINT
351 LET T=0
352 LET S=0 
353 IF R = 2 THEN GOTO 360
355 PRINT "What's the per cent hit rate of enemy gunners (10 TO 50)?"
356 INPUT S
357 IF S < 10 THEN PRINT "YOU LIE, BUT YOU'LL PAY..."
358 IF S < 10 THEN GOTO 380
359 PRINT
360 PRINT
362 IF R > 1 THEN LET T=35
365 IF S+T > 100 * RND THEN GOTO 380
370 PRINT "You made it through tremendous FLAK!"
375 GOTO 390
380 PRINT "* * * * BOOM * * * *"
384 PRINT "YOU HAVE BEEN SHOT DOWN....."
386 PRINT "Dearly beloved, we are gathered here today to pay our"
387 PRINT "last tribute...."
390 PRINT
391 PRINT
392 PRINT "Another mission? (Y or N)"
393 INPUT U$
395 IF U$ = "Y" THEN GOTO 8
400 PRINT "CHICKEN!!" 
410 END

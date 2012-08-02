10 REM Game of Life
20 PRINT "Conway's Game of Life"
30 PRINT "Copyright Adrian McMenamin, 2012"
35 PRINT "adrianmcmenamin@gmail.com"
40 PRINT "Licensed under the GPL version 3"
50 DIM A(48, 70)
60 DIM B$(24)
70 PRINT "Please enter your pattern"
75 PRINT " - up to 24 lines of 70 characters"
80 FOR I = 1 TO 24
90 INPUT B$(I)
95 LET T = 0
97 IF B$(I) = "DONE" THEN LET T = 1
98 IF T = 1 THEN LET B$(I) = ""
100 IF T = 1  THEN GOTO 150
110 PRINT B$(I)
120 NEXT I
150 REM Parse Input
160 LET P = 0
170 LET G = 0
175 LET RR = 0
177 LET NN = 0
180 FOR Y = 1 TO 24
185 LET RR = Y
190 LET Z = LEN B$(Y)
210 IF Z = 0 THEN NEXT Y
220 FOR Q = 1 TO Z
225 LET NN = Q
228 LET A(Y, Q) = 0
230 IF MID$(B$(Y), Q, 1) = " " THEN LET A(Y + 24, Q) = 0
235 IF MID$(B$(Y), Q, 1) <> " " THEN GOSUB 10000
240 NEXT Q
250 FOR Q = Z + 1 TO 70
260 LET A(Y, Q) = 0
265 LET A(Y + 24, Q) = 0
270 NEXT Q
280 NEXT Y
300 REM Display Map
310 PRINT
320 PRINT
330 PRINT
340 PRINT "Generation ", G, " Population is ", P
350 FOR M = 1 TO 24
355 PRINT
360 FOR N = 1 TO 70
370 IF A(M + 24, N) = 1 THEN PRINT "*";
371 IF A(M + 24, N) = 1 THEN PLOT(N, M)
375 IF A(M + 24, N) <> 1 THEN PRINT " ";
376 IF A(M + 24, N) <> 1 THEN UNPLOT(N, M)
380 NEXT N
390 NEXT M
400 REM Map next generation
410 FOR M = 1 TO 24
420 FOR N = 1 TO 70
430 LET A(M, N) = 0
440 IF M + 1 < 25 AND A(M + 25, N) = 1 THEN LET A(M, N) = A(M, N) + 1
450 IF M - 1 > 0 AND A(M + 23, N) = 1 THEN LET A(M, N) = A(M, N) + 1
460 IF N + 1 < 71 AND A(M + 24, N + 1) = 1 THEN LET A(M, N) = A(M, N) + 1
470 IF N - 1 > 0 AND A(M + 24, N - 1) = 1 THEN LET A(M, N) = A(M, N) + 1
480 IF M - 1 > 0 AND N - 1 > 0 AND A(M + 23, N - 1) = 1 THEN LET A(M, N) = A(M, N) + 1
490 IF M - 1 > 0 AND N + 1 < 71 AND A(M + 23, N + 1) = 1 THEN LET A(M, N) = A(M, N) + 1
500 IF M + 1 < 25 AND N - 1 > 0 AND A(M + 25, N - 1) = 1 THEN LET A(M, N) = A(M, N) + 1
510 IF M + 1 < 25 AND N + 1 < 71 AND A(M + 25, N + 1) = 1 THEN LET A(M, N) = A(M, N) + 1
520 NEXT N
530 NEXT M
540 LET P = 0
600 FOR M = 1 TO 24
610 FOR N = 1 TO 70
611 LET ZZ = 0
612 LET SC = A(M, N)
612 IF A(M + 24, N) = 1 THEN LET ZZ = 1
613 LET RES = 0
614 IF ZZ = 0 AND SC = 3 THEN LET RES = 1
615 IF ZZ = 1 AND (SC = 2 OR SC = 3) THEN LET RES = 1
616 LET A(M + 24, N) = RES
617 LET P = P + RES
650 NEXT N
660 NEXT M
700 PAUSE 50000
800 LET G = G + 1
900 GOTO 310
10000 LET A(RR, NN) = 1
10010 LET A(RR + 24, NN) = 1
10020 LET P = P + 1
10030 RETURN

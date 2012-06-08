10 REM Fibonacci sequence
20 LET X = 0
30 LET Y = 1
40 LET I = 0
50 PRINT "Iteration ", I, " value is ", X
60 LET TEMP = X
70 LET X = Y
80 LET Y = Y + TEMP
90 LET I = I + 1
92 IF X < 1000000 THEN GOTO 50
95 IF X < 10000 THEN GOTO 50 ELSE GOTO 100
100 PRINT "Sequence generation finished."
10 REM Fibonacci sequence
20 LET X = 0
30 LET Y = 1
40 LET I = 0
50 PRINT "Iteration ", I, " value is ", X
60 LET TEMP = X
70 LET X = Y
80 LET Y = Y + TEMP
90 LET I = I + 1
95 IF X < 10000000 THEN GOTO 50 ELSE PRINT "All over now"
96 FOR J = I + 1000 TO I * 4 STEP I - 34 
97 PRINT "J is ", J
98 FOR K = 1 TO 3
99 PRINT "K is", K
100 PRINT "Sequence generation finished."
110 NEXT K
120 NEXT J
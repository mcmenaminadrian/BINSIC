10 REM Fibonacci Series
20 PRINT "Here we go"
20 DIM X$(10)
30 LET S = 0
40 LET T = 1
50 FOR I = 0 TO 9
60 LET X$(I) = S + T
80 LET S = T
90 LET T = X$(I)
100 PRINT X$(I)
110 NEXT I


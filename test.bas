10 REM Fibonacci Series
20 DIM X$(10)
30 LET S = 0
40 LET T = 1
50 FOR I = 0 TO 9
60 LET X$(I) = S + T
80 LET S = T
90 LET T = X$(I)
100 NEXT I
110 FOR J = 0 TO 9
120 PRINT "Fibonnaci series element ", J, " is ", X(J)
130 NEXT J

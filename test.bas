10 REM Fibonacci Series
20 LET X = 0.0
25 LET Y = 1.0
29 RAND
31 PRINT "How many numbers in series?"
32 INPUT FIBMAX
35 PRINT "Thanks"
40 LET COUNT = 0
50 DIM Z(FIBMAX)
60 GOSUB 110
62 FOR I = 1 TO FIBMAX
64 PRINT "Fibonacci number ", I, " is ", Z(I - 1)
66 NEXT I
70 END
110 IF COUNT = FIBMAX THEN RETURN
120 LET Z(COUNT) = X + Y
130 LET X = Y
140 LET Y = Z(COUNT)
160 LET COUNT = COUNT + 1
170 GOSUB 110


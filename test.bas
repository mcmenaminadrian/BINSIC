10 REM Fibonacci Series
20 LET X = 0.0
30 LET Y = 1.0
31 PRINT "How many numbers in series?"
32 INPUT FIBMAX
35 PRINT "Thanks"
36 PRINT "Now input some string, please"
37 INPUT A$
38 PRINT "You entered ", A$
40 LET COUNT = 0
50 DIM Z(FIBMAX)
60 GOSUB 110
62 FOR I = 1 TO FIBMAX
64 PRINT "Fibonacci number ", I, " in series called ", A$," is ", Z(I - 1)
66 NEXT I
70 END
110 IF (COUNT >= FIBMAX) THEN RETURN
120 LET Z(COUNT) = X + Y
130 LET X = Y
140 LET Y = Z(COUNT)
160 LET COUNT = COUNT + 1
170 GOSUB 110


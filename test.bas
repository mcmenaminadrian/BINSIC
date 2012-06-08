10 REM Fibonacci sequence
20 LET x = 0
30 LET y = 1
40 LET i = 0
50 PRINT "Iteration ", i, " value is ", x
60 LET temp = x
70 LET x = y
80 LET y = y + temp
90 LET i = i + 1
95 IF x < 10000 THEN GOTO 50




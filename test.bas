10 REM Some Testing code
20 FOR I = 0 TO 31
30 LET Y = SIN(I/10)
35 PRINT " X is ", I/10, "Y is ", Y
40 PLOT (I, 220 - Y)
50 NEXT I
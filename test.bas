10 REM Some Testing code
20 LET X = 0
30 LET Y = 1
40 LET Z = X AND Y
50 LET A = 100
60 PRINT "Z is ", Z, "Z = ",X," AND", Y
70 FOR I = A TO 0 STEP -1
75 PRINT I,": ", SIN(I)," ",TAN(I)
80 IF SIN(I) > 0.5 AND TAN(I) > 0.75 THEN PRINT "BIG" ELSE PRINT "SMALL: ", CHR$(ABS(TAN(I)))
85 PRINT "Tan(I) is ", TAN(I), " and integer value is ", INT(TAN(I)) 
90 NEXT I

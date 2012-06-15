10 REM Fibonacci Series
20 PRINT "Here we go"
20 DIM X$(1000, 10, 1000)
22 FOR I = 0 TO 999
30 LET X$(999 - I, 3, I) = I * 5
40 PRINT "The answer is:",X$(999 - I, 3, I)
50 NEXT I


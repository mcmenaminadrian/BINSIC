10 REM Some Testing code
20 LET A$ = "0123456789ABCDEF0123456789ABCDEF"
35 MID$(A$, 3, 8) = "Jimminies"
40 PRINT "A$ is ", A$
50 PRINT "Code is ", CODE(A$)
10 REM Testing Plotting
20 FOR X = 0 TO 31
30 LET Y = SIN(X/10)
40 PLOT (X, 11 - Y * 11)
50 NEXT X
60 PRINT "So how was that?"
70 INPUT Z
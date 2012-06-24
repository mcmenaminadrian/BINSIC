2 PRINT "The Game of Life"
4 PRINT "Creative Computing Morristown, New Jersey (c) 1978"
6 PRINT
7 PRINT
8 PRINT "Enter your pattern:"
9 LET X1=1
10 DIM A(24,70)
11 DIM B$(24)
12 LET Y1=1
13 LET X2=24
14 LET Y2=70
20 LET C=1
30 INPUT B$(C)
40 IF B$(C) = "DONE" THEN B$(C)=""
45 IF B$(C) = "DONE" THEN GOTO 80
50 PRINT B$(C)
60 LET C=C+1
70 GOTO 30
80 LET C=C-1
85 LET L=0
90 FOR X=1 TO C-1
100 IF LEN(B$(X))>L THEN LET L=LEN(B$(X))
110 NEXT X
120 LET X1=11-C/2
130 LET Y1=33-L/2
140 FOR X=1 TO C
150 FOR Y=1 TO LEN(B$(X))
160 IF MID$(B$(X),Y,1)<>" " THEN LET A(X1+X,Y1+Y)=1
165 IF MID$(B$(X),Y,1)<>" " THEN LET P=P+1
170 NEXT Y
180 NEXT X
200 PRINT
202 PRINT
203 PRINT
210 PRINT "Genertion: ", G," Population: ", P;
212 IF I9 THEN PRINT "Invalid!"
215 LET X3=24
216 LET Y3=70
217 LET X4=1
218 LET Y4=1
219 LET P=0
220 LET G=G+1
225 FOR X=1 TO X1-1
226 PRINT
227 NEXT X
230 FOR X=X1 TO X2
240 PRINT
250 FOR Y=Y1 TO Y2
253 IF A(X,Y) = 2 THEN LET A(X,Y)=0
254 IF A(X,Y) = 2 THEN GOTO 270
256 IF A(X,Y) = 3 THEN LET A(X,Y)=1
257 IF A(X,Y) = 3 THEN GOTO 261
260 IF A(X,Y)<>1 THEN GOTO 270
261 PRINT TAB(Y), "*"
262 IF X<X3 THEN LET X3=X
264 IF X>X4 THEN LET X4=X
266 IF Y<Y3 THEN LET Y3=Y
268 IF Y>Y4 THEN LET Y4=Y
270 NEXT Y
290 NEXT X
292 FOR X=X2+1 TO 24
293 PRINT
294 NEXT X
295 LET X1=X3
296 LET X2=X4
297 LET Y1=Y3
298 LET Y2=Y4
301 IF X1<3 THEN LET X1=3
302 IF X1 < 3 THEN LET I9=-1
303 IF X2 > 22 THEN LET X2=22
304 IF X2 > 22 THEN LET I9=-1
305 IF Y1<3 THEN LET Y1=3
304 IF Y1<3 THEN LET I9=-1
307 IF Y2>68 THEN LET Y2=68
308 IF Y2>68 THEN LET I9=-1
309 LET P=0
500 FOR X=X1-1 TO X2+1
510 FOR Y=Y1-1 TO Y2+1
520 LET C=0
530 FOR I=X-1 TO X+1
540 FOR J=Y-1 TO Y+1
550 IF A(I,J) = 1 OR A(I,J) = 2 THEN LET C = C+1
560 NEXT J
570 NEXT I
580 IF A(X,Y) = 0 THEN GOTO 610
590 IF C<3 OR C>4 THEN LET A(X,Y)=2
592 IF C<3 OR C>4 THEN GOTO 600
595 LET P=P+1
600 GOTO 620
610 IF C = 3 THEN LET A(X,Y)=3
612 IF C = 3 THEN P=P+1
620 NEXT Y
630 NEXT X
635 LET X1=X1-1
636 LET Y1=Y1-1
637 LET X2=X2+1
638 LET Y2=Y2+1
640 GOTO 210
650 END
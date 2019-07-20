        IDENTIFICATION  DIVISION.
        PROGRAM-ID. FCBOOK.
        DATA DIVISION.
        WORKING-STORAGE SECTION.
         01 RESP-CODE         PIC s9(8) COMP-5 SYNC.
         01 RESP-STR          PIC X(8) VALUE SPACES.
         01 RESP2-STR         PIC X(8) VALUE SPACES.
         01 RESP2-CODE        PIC s9(8) COMP-5 SYNC.
         01 BOOK-DATA.
            03 BOOK-ID    PIC X(8).
            03 BOOK-NAME  PIC X(56).
            03 PAGECOUNT  PIC S9(8) COMP. 
            03 AUTHR-ID   PIC X(10).
            03 RESSERVE   PIC X(2).
       PROCEDURE DIVISION.
            PERFORM START-GETDATA THRU START-PUTDATA.
            GOBACK.
      *****************************************************************
      * LINK FROM JAVA PROGRAM
      *****************************************************************
       START-GETDATA.

            MOVE SPACES TO BOOK-DATA

            EXEC CICS GET CONTAINER('QUARY-BOOKDATA')
                          CHANNEL('QUARY-BOOK')
                          INTO(BOOK-DATA)
                          END-EXEC

            EXIT.

       START-PUTDATA.

            EXEC CICS READ FILE('BOOK')
                           INTO(BOOK-DATA)
                           RIDFLD(BOOK-ID)
                           RESP(RESP-CODE)
                           END-EXEC
            MOVE 9999 TO PAGECOUNT
            IF RESP-CODE = 0 THEN
            EXEC CICS PUT CONTAINER('QUARY-BOOKDATA')
                          CHANNEL('QUARY-BOOK')
                          FROM(BOOK-DATA)
                          END-EXEC
            END-IF

            EXIT.
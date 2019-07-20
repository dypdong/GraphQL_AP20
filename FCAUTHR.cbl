        IDENTIFICATION  DIVISION.
        PROGRAM-ID. FCAUTHR.
        DATA DIVISION.
        WORKING-STORAGE SECTION.
         01 RESP-CODE         PIC s9(8) COMP-5 SYNC.
         01 RESP-STR          PIC X(8) VALUE SPACES.
         01 RESP2-STR         PIC X(8) VALUE SPACES.
         01 RESP2-CODE        PIC s9(8) COMP-5 SYNC.
         01 AUTHOR-DATA.
            03 AUTHOR-ID   PIC X(10).
            03 FIRST-NAME  PIC X(16).
            03 LAST-NAME   PIC X(16).
            03 RESSERVE    PIC X(38).
       PROCEDURE DIVISION.
            PERFORM START-GETDATA THRU START-PUTDATA.
            GOBACK.
      *****************************************************************
      * LINK FROM JAVA PROGRAM
      *****************************************************************
       START-GETDATA.

            MOVE SPACES TO AUTHOR-DATA

            EXEC CICS GET CONTAINER('QUARY-AUTHRDATA')
                          CHANNEL('QUARY-BOOK')
                          INTO(AUTHOR-DATA)
                          END-EXEC

            EXIT.

       START-PUTDATA.

            EXEC CICS READ FILE('AUTHOR')
                           INTO(AUTHOR-DATA)
                           RIDFLD(AUTHOR-ID)
                           RESP(RESP-CODE)
                           END-EXEC

            IF RESP-CODE = 0 THEN
            EXEC CICS PUT CONTAINER('QUARY-AUTHRDATA')
                          CHANNEL('QUARY-BOOK')
                          FROM(AUTHOR-DATA)
                          END-EXEC
            END-IF

            EXIT.
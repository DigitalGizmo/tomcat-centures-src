BULK INSERT OhTopic 
    FROM 'G:\sites\centuries\OralHistories\OhTopic.txt' 
    WITH 
    ( 
        FIRSTROW = 2, 
        FIELDTERMINATOR = '\t', 
        ROWTERMINATOR = '\n' 
    )

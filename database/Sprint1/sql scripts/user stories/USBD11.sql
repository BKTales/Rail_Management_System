SELECT "Wagon".*
FROM "Wagon"
INNER JOIN "Wagon Model" ON "Wagon".wagonModelId = "Wagon Model".wagonModelId
WHERE "Wagon Model".gaugeId = 2;
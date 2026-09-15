SELECT trackTypeId, AVG(length)
FROM "Rail Line Segment"
WHERE railLineId = 4
GROUP BY trackTypeId;
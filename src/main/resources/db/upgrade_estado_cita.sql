SET XACT_ABORT ON;
BEGIN TRANSACTION;

IF COL_LENGTH('dbo.cita', 'estado_cita') IS NULL
BEGIN
    ALTER TABLE dbo.cita
        ADD estado_cita varchar(20) NOT NULL
            CONSTRAINT DF_cita_estado_cita DEFAULT ('CONFIRMADA');
END
ELSE
BEGIN
    UPDATE dbo.cita
    SET estado_cita = 'CONFIRMADA'
    WHERE estado_cita IS NULL;

    ALTER TABLE dbo.cita
        ALTER COLUMN estado_cita varchar(20) NOT NULL;

    IF NOT EXISTS (
        SELECT 1
        FROM sys.default_constraints
        WHERE parent_object_id = OBJECT_ID('dbo.cita')
          AND parent_column_id = COLUMNPROPERTY(OBJECT_ID('dbo.cita'), 'estado_cita', 'ColumnId')
    )
    BEGIN
        ALTER TABLE dbo.cita
            ADD CONSTRAINT DF_cita_estado_cita
                DEFAULT ('CONFIRMADA') FOR estado_cita;
    END
END;

DECLARE @uniqueConstraint sysname;

SELECT TOP (1) @uniqueConstraint = keyConstraint.name
FROM sys.key_constraints AS keyConstraint
JOIN sys.index_columns AS firstKey
    ON firstKey.object_id = keyConstraint.parent_object_id
   AND firstKey.index_id = keyConstraint.unique_index_id
   AND firstKey.key_ordinal = 1
JOIN sys.columns AS firstColumn
    ON firstColumn.object_id = firstKey.object_id
   AND firstColumn.column_id = firstKey.column_id
JOIN sys.index_columns AS secondKey
    ON secondKey.object_id = keyConstraint.parent_object_id
   AND secondKey.index_id = keyConstraint.unique_index_id
   AND secondKey.key_ordinal = 2
JOIN sys.columns AS secondColumn
    ON secondColumn.object_id = secondKey.object_id
   AND secondColumn.column_id = secondKey.column_id
WHERE keyConstraint.parent_object_id = OBJECT_ID('dbo.cita')
  AND keyConstraint.type = 'UQ'
  AND firstColumn.name = 'fecha_cita'
  AND secondColumn.name = 'hora_cita'
  AND (
      SELECT COUNT(*)
      FROM sys.index_columns AS keyColumns
      WHERE keyColumns.object_id = keyConstraint.parent_object_id
        AND keyColumns.index_id = keyConstraint.unique_index_id
        AND keyColumns.key_ordinal > 0
  ) = 2;

IF @uniqueConstraint IS NOT NULL
BEGIN
    EXEC (N'ALTER TABLE dbo.cita DROP CONSTRAINT ' + QUOTENAME(@uniqueConstraint));
END;

IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE object_id = OBJECT_ID('dbo.cita')
      AND name = 'UX_cita_fecha_hora_activa'
)
BEGIN
    CREATE UNIQUE INDEX UX_cita_fecha_hora_activa
        ON dbo.cita (fecha_cita, hora_cita)
        WHERE estado_cita <> 'RECHAZADA';
END;

COMMIT TRANSACTION;

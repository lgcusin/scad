--<ScriptOptions statementTerminator=";"/>

CREATE PROCEDURE BUSCAR_ID (
)
	NOT DETERMINISTIC
	NO_SQL;

CREATE PROCEDURE RESET_SEQ (
)
	NOT DETERMINISTIC
	NO_SQL;

CREATE PROCEDURE SP_EVAL_RES_DOCENTE (
)
	NOT DETERMINISTIC
	NO_SQL;

CREATE PROCEDURE SP_REC_ACAD_GRADO (
)
	NOT DETERMINISTIC
	NO_SQL;

CREATE PROCEDURE SP_EVAL_RES_CARRERA (
)
	NOT DETERMINISTIC
	NO_SQL;

CREATE PROCEDURE SP_CRGHR_1919 (
		IN VCARRERA VARCHAR2(0),
		OUT VRESSULTADO null)
	NOT DETERMINISTIC
	NO_SQL;

CREATE PROCEDURE RESET_SEQUENCE (
		IN SEQ_NAME VARCHAR2(0),
		IN STARTVALUE null)
	NOT DETERMINISTIC
	NO_SQL;


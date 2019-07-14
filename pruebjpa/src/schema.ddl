
    create table DESARROLLO.SYLLABO (
        SYL_ID number(10,0) not null,
        SYL_CONTRIBUCION_PROFESIONAL varchar2(700 char),
        SYL_DESCRIPCION varchar2(700 char),
        SYL_HORAS_CLASE number(10,0),
        SYL_HORAS_TUTORIAS number(10,0),
        SYL_OBJETIVO_ESPC varchar2(700 char),
        SYL_OBJETIVO_GNRL varchar2(700 char),
        SYL_RESULTADOS_APRENDIZAJE varchar2(700 char),
        primary key (SYL_ID)
    );

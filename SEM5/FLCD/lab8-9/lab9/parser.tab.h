
/* A Bison parser, made by GNU Bison 2.4.1.  */

/* Skeleton interface for Bison's Yacc-like parsers in C
   
      Copyright (C) 1984, 1989, 1990, 2000, 2001, 2002, 2003, 2004, 2005, 2006
   Free Software Foundation, Inc.
   
   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.
   
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   
   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.
   
   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */


/* Tokens.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
   /* Put the tokens into the symbol table, so that GDB and other debuggers
      know about them.  */
   enum yytokentype {
     IDENTIFIER = 258,
     STRING_CONST = 259,
     CHAR_CONST = 260,
     NUMBER_CONST = 261,
     INT = 262,
     FLOAT = 263,
     STRING = 264,
     CHAR = 265,
     READ = 266,
     WRITE = 267,
     IF = 268,
     ELIF = 269,
     ELSE = 270,
     FOR = 271,
     WHILE = 272,
     RETURN = 273,
     START = 274,
     END = 275,
     ARRAY = 276,
     TAKES = 277,
     SMALLER = 278,
     GREATER = 279,
     SMALLER_EQ = 280,
     GREATER_EQ = 281,
     IS = 282,
     IS_NOT = 283,
     PLUS = 284,
     MINUS = 285,
     MUL = 286,
     DIV = 287,
     MOD = 288,
     LEFT_CURLY_BRACKET = 289,
     RIGHT_CURLY_BRACKET = 290,
     LEFT_ROUND_BRACKET = 291,
     RIGHT_ROUND_BRACKET = 292,
     LEFT_BRACKET = 293,
     RIGHT_BRACKET = 294,
     COLON = 295,
     SEMICOLON = 296,
     COMMA = 297,
     SQRT = 298
   };
#endif



#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef union YYSTYPE
{

/* Line 1676 of yacc.c  */
#line 12 "parser.y"

    char* str;



/* Line 1676 of yacc.c  */
#line 101 "parser.tab.h"
} YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define yystype YYSTYPE /* obsolescent; will be withdrawn */
# define YYSTYPE_IS_DECLARED 1
#endif

extern YYSTYPE yylval;



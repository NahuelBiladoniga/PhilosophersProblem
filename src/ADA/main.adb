with Ada.Numerics.Discrete_Random;
with Ada.Text_IO;                       use Ada.Text_IO;
with Ada.Integer_Text_IO;           use Ada.Integer_Text_IO;

procedure Main is
   subtype Int is Integer range 0 .. 4;

   task type tenedor is
      entry tomar;
      entry devolver;
   end tenedor;
   task body tenedor is
   begin
      loop
         accept tomar;
         accept devolver;
      end loop;
   end tenedor;

   task type comedor is
      entry entrar;
      entry salir;
   end comedor;
   task body comedor is
      n:Int:=0;
   begin
      loop
         select
            when (n<4)=>
               accept entrar;
               n:=n+1;
         or
            accept salir;
            n:=n-1;
         end select;
      end loop;
   end comedor;

   task numerador is
      entry dmn(n:out Int);
   end numerador;
   task body numerador is
   begin
      for i in 0 .. 4 loop
         accept dmn(n:out Int) do
           n:=i;
         end dmn;
      end loop;
   end numerador;

   subtype Rand_Range is Positive;
   package Rand_Int is new Ada.Numerics.Discrete_Random(Rand_Range);

   gen : Rand_Int.Generator;

   function random ( n: in Positive) return Integer is
   begin
      return Rand_Int.Random(gen) mod n;  -- or mod n+1 to include the end value
   end random;

   procedure pensar is
   begin
      Rand_Int.Reset(gen);
      delay Duration(random(10));
   end pensar;

   procedure comer is
   begin
      Rand_Int.Reset(gen);
      delay Duration(random(10));
   end comer;

   task type filosofo is
   end filosofo;
   task body filosofo is
      izq,der:Int;
      tenedores : array (0 .. 4) of tenedor;
      com:comedor;
   begin
      numerador.dmn(izq);
      der:=(izq+1) mod 5;
      loop
         Put("El ");
         Put(izq);
         Put(" filosofo esta pensando");
         New_Line;
         pensar;
         com.entrar;
         tenedores(izq).tomar;
         tenedores(der).tomar;
         Put("El ");
         Put(izq);
         Put(" filosofo esta comiendo");
         New_Line;
         comer;
         tenedores(der).devolver;
         tenedores(izq).devolver;
         com.salir;
      end loop;
   end ;
   filosofos:array (0 .. 4) of filosofo;
begin
   null;
end Main;

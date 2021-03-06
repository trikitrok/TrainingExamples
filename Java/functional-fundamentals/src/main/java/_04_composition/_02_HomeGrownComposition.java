package _04_composition;

import _04_composition.wrappers.Arithmetics;

import static _04_composition.wrappers.Compose.compose;
import static _04_composition.wrappers.Functions.f0;
import static java.lang.System.out;

public class _02_HomeGrownComposition {
    public static void main(String[] args) {

        //DESCRIBE HOMEGROWN compose FUNCTION

        //WITHOUT COMPOSITION - EVALUATION
        out.println(0 + 1 + 1 + 1 + 1);
        out.println((((0 + 1) + 1) + 1) + 1);

        //WITHOUT COMPOSITION - LAZY EVALUATION
        out.println(f0(() -> (((0 + 1) + 1) + 1) + 1).apply());

        //SIMPLE FUNCTION COMPOSITION
        out.println(compose(Arithmetics::plus1, Arithmetics::plus1).apply(0));

        //NESTED FUNCTION COMPOSITION #1
        out.println(
            compose(
                Arithmetics::plus1,
                compose(Arithmetics::plus1, Arithmetics::plus1)
            ).apply(0));

        //NESTED FUNCTION COMPOSITION #2
        out.println(
            compose(
                Arithmetics::plus1,
                compose(
                    Arithmetics::plus1,
                    compose(
                        Arithmetics::plus1,
                        Arithmetics::plus1)
                )
            ).apply(0));

        //NESTED FUNCTION COMPOSITION #3
        out.println(
            compose(
                Arithmetics::plus1,
                compose(
                    Arithmetics::plus1,
                    compose(
                        Arithmetics::plus1,
                        compose(
                            Arithmetics::plus1,
                            Arithmetics::plus1)
                    )
                )
            ).apply(0));
    }
}

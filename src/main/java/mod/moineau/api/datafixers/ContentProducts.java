package mod.moineau.api.datafixers;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.K1;

public class ContentProducts {
    public static final class P31<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16,
            T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31> {
        private final App<F, T1> t1;
        private final App<F, T2> t2;
        private final App<F, T3> t3;
        private final App<F, T4> t4;
        private final App<F, T5> t5;
        private final App<F, T6> t6;
        private final App<F, T7> t7;
        private final App<F, T8> t8;
        private final App<F, T9> t9;
        private final App<F, T10> t10;
        private final App<F, T11> t11;
        private final App<F, T12> t12;
        private final App<F, T13> t13;
        private final App<F, T14> t14;
        private final App<F, T15> t15;
        private final App<F, T16> t16;
        private final App<F, T17> t17;
        private final App<F, T18> t18;
        private final App<F, T19> t19;
        private final App<F, T20> t20;
        private final App<F, T21> t21;
        private final App<F, T22> t22;
        private final App<F, T23> t23;
        private final App<F, T24> t24;
        private final App<F, T25> t25;
        private final App<F, T26> t26;
        private final App<F, T27> t27;
        private final App<F, T28> t28;
        private final App<F, T29> t29;
        private final App<F, T30> t30;
        private final App<F, T31> t31;

        public P31(
                final App<F, T1> t1,
                final App<F, T2> t2,
                final App<F, T3> t3,
                final App<F, T4> t4,
                final App<F, T5> t5,
                final App<F, T6> t6,
                final App<F, T7> t7,
                final App<F, T8> t8,
                final App<F, T9> t9,
                final App<F, T10> t10,
                final App<F, T11> t11,
                final App<F, T12> t12,
                final App<F, T13> t13,
                final App<F, T14> t14,
                final App<F, T15> t15,
                final App<F, T16> t16,
                final App<F, T17> t17,
                final App<F, T18> t18,
                final App<F, T19> t19,
                final App<F, T20> t20,
                final App<F, T21> t21,
                final App<F, T22> t22,
                final App<F, T23> t23,
                final App<F, T24> t24,
                final App<F, T25> t25,
                final App<F, T26> t26,
                final App<F, T27> t27,
                final App<F, T28> t28,
                final App<F, T29> t29,
                final App<F, T30> t30,
                final App<F, T31> t31
        ) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
            this.t7 = t7;
            this.t8 = t8;
            this.t9 = t9;
            this.t10 = t10;
            this.t11 = t11;
            this.t12 = t12;
            this.t13 = t13;
            this.t14 = t14;
            this.t15 = t15;
            this.t16 = t16;
            this.t17 = t17;
            this.t18 = t18;
            this.t19 = t19;
            this.t20 = t20;
            this.t21 = t21;
            this.t22 = t22;
            this.t23 = t23;
            this.t24 = t24;
            this.t25 = t25;
            this.t26 = t26;
            this.t27 = t27;
            this.t28 = t28;
            this.t29 = t29;
            this.t30 = t30;
            this.t31 = t31;
        }

        public <R> App<F, R> apply(
                final Applicative<F, ?> instance,
                final Function31<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16,
                        T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, R> function
        ) {
            return apply(instance, instance.point(function));
        }

        public <R> App<F, R> apply(
                final Applicative<F, ?> instance,
                final App<F, Function31<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16,
                        T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, R>> function
        ) {
            return ap31(instance, function, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16,
                    t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28, t29, t30, t31);
        }

        public static <F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16,
                T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, R> App<F, R> ap31(
                final Applicative<F, ?> instance,
                final App<F, Function31<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16,
                        T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, R>> func,
                final App<F, T1> t1,
                final App<F, T2> t2,
                final App<F, T3> t3,
                final App<F, T4> t4,
                final App<F, T5> t5,
                final App<F, T6> t6,
                final App<F, T7> t7,
                final App<F, T8> t8,
                final App<F, T9> t9,
                final App<F, T10> t10,
                final App<F, T11> t11,
                final App<F, T12> t12,
                final App<F, T13> t13,
                final App<F, T14> t14,
                final App<F, T15> t15,
                final App<F, T16> t16,
                final App<F, T17> t17,
                final App<F, T18> t18,
                final App<F, T19> t19,
                final App<F, T20> t20,
                final App<F, T21> t21,
                final App<F, T22> t22,
                final App<F, T23> t23,
                final App<F, T24> t24,
                final App<F, T25> t25,
                final App<F, T26> t26,
                final App<F, T27> t27,
                final App<F, T28> t28,
                final App<F, T29> t29,
                final App<F, T30> t30,
                final App<F, T31> t31
        ) {
            return instance.ap16(instance.ap15(instance.map(Function31::curry15, func), t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15),
                    t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28, t29, t30, t31);
        }

        public static <F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16,
                T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31>
        P31<F, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16,
                T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31> group(
                final Applicative<F, ?> instance,
                final App<F, T1> t1,
                final App<F, T2> t2,
                final App<F, T3> t3,
                final App<F, T4> t4,
                final App<F, T5> t5,
                final App<F, T6> t6,
                final App<F, T7> t7,
                final App<F, T8> t8,
                final App<F, T9> t9,
                final App<F, T10> t10,
                final App<F, T11> t11,
                final App<F, T12> t12,
                final App<F, T13> t13,
                final App<F, T14> t14,
                final App<F, T15> t15,
                final App<F, T16> t16,
                final App<F, T17> t17,
                final App<F, T18> t18,
                final App<F, T19> t19,
                final App<F, T20> t20,
                final App<F, T21> t21,
                final App<F, T22> t22,
                final App<F, T23> t23,
                final App<F, T24> t24,
                final App<F, T25> t25,
                final App<F, T26> t26,
                final App<F, T27> t27,
                final App<F, T28> t28,
                final App<F, T29> t29,
                final App<F, T30> t30,
                final App<F, T31> t31
        ) {
            return new P31<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16,
                    t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28, t29, t30, t31);
        }
    }

    public static final class P32<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16,
            T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32> {
        private final App<F, T1> t1;
        private final App<F, T2> t2;
        private final App<F, T3> t3;
        private final App<F, T4> t4;
        private final App<F, T5> t5;
        private final App<F, T6> t6;
        private final App<F, T7> t7;
        private final App<F, T8> t8;
        private final App<F, T9> t9;
        private final App<F, T10> t10;
        private final App<F, T11> t11;
        private final App<F, T12> t12;
        private final App<F, T13> t13;
        private final App<F, T14> t14;
        private final App<F, T15> t15;
        private final App<F, T16> t16;
        private final App<F, T17> t17;
        private final App<F, T18> t18;
        private final App<F, T19> t19;
        private final App<F, T20> t20;
        private final App<F, T21> t21;
        private final App<F, T22> t22;
        private final App<F, T23> t23;
        private final App<F, T24> t24;
        private final App<F, T25> t25;
        private final App<F, T26> t26;
        private final App<F, T27> t27;
        private final App<F, T28> t28;
        private final App<F, T29> t29;
        private final App<F, T30> t30;
        private final App<F, T31> t31;
        private final App<F, T32> t32;

        public P32(
                final App<F, T1> t1,
                final App<F, T2> t2,
                final App<F, T3> t3,
                final App<F, T4> t4,
                final App<F, T5> t5,
                final App<F, T6> t6,
                final App<F, T7> t7,
                final App<F, T8> t8,
                final App<F, T9> t9,
                final App<F, T10> t10,
                final App<F, T11> t11,
                final App<F, T12> t12,
                final App<F, T13> t13,
                final App<F, T14> t14,
                final App<F, T15> t15,
                final App<F, T16> t16,
                final App<F, T17> t17,
                final App<F, T18> t18,
                final App<F, T19> t19,
                final App<F, T20> t20,
                final App<F, T21> t21,
                final App<F, T22> t22,
                final App<F, T23> t23,
                final App<F, T24> t24,
                final App<F, T25> t25,
                final App<F, T26> t26,
                final App<F, T27> t27,
                final App<F, T28> t28,
                final App<F, T29> t29,
                final App<F, T30> t30,
                final App<F, T31> t31,
                final App<F, T32> t32
        ) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
            this.t7 = t7;
            this.t8 = t8;
            this.t9 = t9;
            this.t10 = t10;
            this.t11 = t11;
            this.t12 = t12;
            this.t13 = t13;
            this.t14 = t14;
            this.t15 = t15;
            this.t16 = t16;
            this.t17 = t17;
            this.t18 = t18;
            this.t19 = t19;
            this.t20 = t20;
            this.t21 = t21;
            this.t22 = t22;
            this.t23 = t23;
            this.t24 = t24;
            this.t25 = t25;
            this.t26 = t26;
            this.t27 = t27;
            this.t28 = t28;
            this.t29 = t29;
            this.t30 = t30;
            this.t31 = t31;
            this.t32 = t32;
        }

        public <R> App<F, R> apply(
                final Applicative<F, ?> instance,
                final Function32<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16,
                        T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, R> function
        ) {
            return apply(instance, instance.point(function));
        }

        public <R> App<F, R> apply(
                final Applicative<F, ?> instance,
                final App<F, Function32<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16,
                        T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, R>> function
        ) {
            return ap32(instance, function, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16,
                    t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28, t29, t30, t31, t32);
        }

        public static <F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16,
                T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, R> App<F, R> ap32(
                final Applicative<F, ?> instance,
                final App<F, Function32<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16,
                        T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, R>> func,
                final App<F, T1> t1,
                final App<F, T2> t2,
                final App<F, T3> t3,
                final App<F, T4> t4,
                final App<F, T5> t5,
                final App<F, T6> t6,
                final App<F, T7> t7,
                final App<F, T8> t8,
                final App<F, T9> t9,
                final App<F, T10> t10,
                final App<F, T11> t11,
                final App<F, T12> t12,
                final App<F, T13> t13,
                final App<F, T14> t14,
                final App<F, T15> t15,
                final App<F, T16> t16,
                final App<F, T17> t17,
                final App<F, T18> t18,
                final App<F, T19> t19,
                final App<F, T20> t20,
                final App<F, T21> t21,
                final App<F, T22> t22,
                final App<F, T23> t23,
                final App<F, T24> t24,
                final App<F, T25> t25,
                final App<F, T26> t26,
                final App<F, T27> t27,
                final App<F, T28> t28,
                final App<F, T29> t29,
                final App<F, T30> t30,
                final App<F, T31> t31,
                final App<F, T32> t32
        ) {
            return instance.ap16(instance.ap16(instance.map(Function32::curry16, func), t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16),
                    t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28, t29, t30, t31, t32);
        }

        public static <F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16,
                T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32>
        P32<F, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16,
                T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32> group(
                final Applicative<F, ?> instance,
                final App<F, T1> t1,
                final App<F, T2> t2,
                final App<F, T3> t3,
                final App<F, T4> t4,
                final App<F, T5> t5,
                final App<F, T6> t6,
                final App<F, T7> t7,
                final App<F, T8> t8,
                final App<F, T9> t9,
                final App<F, T10> t10,
                final App<F, T11> t11,
                final App<F, T12> t12,
                final App<F, T13> t13,
                final App<F, T14> t14,
                final App<F, T15> t15,
                final App<F, T16> t16,
                final App<F, T17> t17,
                final App<F, T18> t18,
                final App<F, T19> t19,
                final App<F, T20> t20,
                final App<F, T21> t21,
                final App<F, T22> t22,
                final App<F, T23> t23,
                final App<F, T24> t24,
                final App<F, T25> t25,
                final App<F, T26> t26,
                final App<F, T27> t27,
                final App<F, T28> t28,
                final App<F, T29> t29,
                final App<F, T30> t30,
                final App<F, T31> t31,
                final App<F, T32> t32
        ) {
            return new P32<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16,
                    t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28, t29, t30, t31, t32);
        }
    }
}

package protobuf.utils;

import com.google.common.base.Strings;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 提供了一系列的静态工厂方法进行校验
 *      private + final
 * @author zhaolei22
 * @date 2020/09/16
 */
public final class Optionals {

    private Optionals(){}

    public static <T> Optional<T> forPredicate(boolean predicate, Supplier<T> supplier){
        return predicate?Optional.ofNullable(supplier.get()):Optional.empty();
    }

    public static <T> Optional<T> forPredicateAndNotNull(boolean predicate, Supplier<T> supplier){
        return predicate? Optionals.forNotNull(supplier):Optional.empty();
    }

    public static Optional<String> forPredicateAndNotBlank(boolean predicate, Supplier<String> supplier){
        return predicate? Optionals.forNotNullAndEmpty(supplier):Optional.empty();
    }

    public static <T> Optional<T> forNotNull(Supplier<T> supplier){
        if(supplier.get() instanceof Collection){
            Collection<?> collection = (Collection<?>) supplier.get();
            if(CollectionUtils.isEmpty(collection)){
                return Optional.empty();
            }
        }

        return Optional.ofNullable(supplier.get());
    }

    public static <T extends Number> Optional<T> forNotNullAndZero(Supplier<T> supplier){
        T t = supplier.get();
        if(t==null||t.intValue()==0){
            return Optional.empty();
        }
        return Optional.of(supplier.get());
    }

    public static Optional<String> forNotNullAndEmpty(Supplier<String> supplier){
        if(Strings.isNullOrEmpty(supplier.get())){
            return Optional.empty();
        }
        return Optional.of(supplier.get());
    }
}

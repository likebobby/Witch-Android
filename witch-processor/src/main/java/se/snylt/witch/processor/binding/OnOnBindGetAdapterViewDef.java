package se.snylt.witch.processor.binding;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

import se.snylt.witch.processor.TypeUtils;

import static se.snylt.witch.processor.PropertytUtils.getPropertySetter;

public class OnOnBindGetAdapterViewDef extends OnBindDef {

    private final String property;

    private final TypeName viewType;

    private final TypeName adapterType;

    private final TypeName valueType;

    public OnOnBindGetAdapterViewDef(String property, TypeName viewType, TypeName adapterType,  TypeName valueType) {
        this.property = property;
        this.viewType = viewType;
        this.adapterType = adapterType;
        this.valueType = valueType;
    }

    @Override
    public String getNewInstanceJava() {
        MethodSpec method = MethodSpec.methodBuilder("onBind")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(viewType, "target")
                .addParameter(valueType, "value")
                .returns(void.class)
                .addStatement("(($T)$N.getAdapter()).$N(($T)value)", adapterType, "target", getPropertySetter(property), valueType)
                .build();

        TypeSpec anonymous = TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(ParameterizedTypeName
                        .get(TypeUtils.SYNC_ON_BIND, viewType, valueType))
                .addMethod(method)
                .build();

        return anonymous.toString();
    }
}

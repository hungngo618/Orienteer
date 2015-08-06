package org.orienteer.core.component.command;

import com.google.common.base.Strings;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.record.impl.ODocument;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.orienteer.core.component.property.DisplayMode;
import org.orienteer.core.component.table.OrienteerDataTable;
import org.orienteer.core.module.OrienteerLocalizationModule;

/**
 * Command to save all OLocalizations and make active those which have both language and value.
 */
public class SaveOLocalizationsCommand extends AbstractSaveCommand<ODocument> {

    private OrienteerDataTable<ODocument, ?> table;

    public SaveOLocalizationsCommand(OrienteerDataTable<ODocument, ?> table,
                                 IModel<DisplayMode> displayModeModel)
    {
        super(table, displayModeModel);
        this.table = table;
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
        table.visitChildren(OrienteerDataTable.MetaContextItem.class, new IVisitor<OrienteerDataTable.MetaContextItem<ODocument, ?>, Void>() {

            @Override
            public void component(OrienteerDataTable.MetaContextItem<ODocument, ?> rowItem, IVisit<Void> visit) {
                ODocument modelObject = rowItem.getModelObject();
                if (modelObject == null) {
                    return;
                }

                if (!Strings.isNullOrEmpty(modelObject.<String> field(OrienteerLocalizationModule.OPROPERTY_LANG))
                        && !Strings.isNullOrEmpty(modelObject.<String> field(OrienteerLocalizationModule.OPROPERTY_VALUE))) {
                    modelObject.field(OrienteerLocalizationModule.OPROPERTY_ACTIVE, true);
                }

                modelObject.save();
                visit.dontGoDeeper();
            }
        });
        super.onClick(target);
    }
}

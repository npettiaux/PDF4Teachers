package fr.themsou.panel.leftBar.texts;

import fr.themsou.main.Main;
import fr.themsou.utils.Builders;
import fr.themsou.utils.NodeMenuItem;
import fr.themsou.utils.TR;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class TextTreeView {


    public TextTreeView(TreeView treeView){

        treeView.setCellFactory(new Callback<TreeView, TreeCell>() {
            @Override public TreeCell call(TreeView param) {
                return new TreeCell<String>() {
                    @Override protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        // Null
                        if(empty){
                            setGraphic(null);
                            setStyle(null);
                            setContextMenu(null);
                            setOnMouseClicked(null);
                            return;
                        }
                        // Category or Sort Options
                        if(item != null){
                            setContextMenu(null);
                            setOnMouseClicked(null);

                            if(item.equals("favoritesOptions")){
                                setStyle("-fx-padding: 0 0 0 -40; -fx-margin: 0; -fx-background-color: #cccccc;");
                                setGraphic(Main.lbTextTab.favoritesTextOptions);
                                return;
                            }if(item.equals("lastsOptions")){
                                setStyle("-fx-padding: 0 0 0 -40; -fx-margin: 0; -fx-background-color: #cccccc;");
                                setGraphic(Main.lbTextTab.lastsTextOptions);
                                return;
                            }if(item.equals("onFileOptions")){
                                setStyle("-fx-padding: 0 0 0 -40; -fx-margin: 0; -fx-background-color: #cccccc;");
                                setGraphic(Main.lbTextTab.onFileTextOptions);
                                return;
                            }

                            HBox box = new HBox();
                            box.setAlignment(Pos.CENTER);
                            setMaxHeight(30);
                            box.setPrefHeight(18);
                            setStyle("-fx-padding: 6 6 6 2; -fx-background-color: #cccccc;");
                            box.setStyle("-fx-padding: -6 -6 -6 0;");

                            Text name = new Text();
                            name.setFont(new Font(14));
                            box.getChildren().add(name);

                            Region spacer = new Region();
                            HBox.setHgrow(spacer, Priority.ALWAYS);
                            box.getChildren().add(spacer);

                            Pane toggle = new Pane();
                            box.getChildren().add(toggle);

                            if(item.equals("favoritesText")){
                                name.setText(TR.tr("Éléments Favoris"));
                                toggle.getChildren().add(Main.lbTextTab.favoritesTextToggleOption);
                                setContextMenu(getCategoryMenu(true));
                            }if(item.equals("lastsText")){
                                name.setText(TR.tr("Éléments Précédents"));
                                box.getChildren().add(Main.lbTextTab.lastsTextToggleOption);
                                setContextMenu(getCategoryMenu(false));
                            }if(item.equals("onFileText")){
                                name.setText(TR.tr("Éléments sur ce document"));
                                box.getChildren().add(Main.lbTextTab.onFileTextToggleOption);
                            }
                            setGraphic(box);

                            return;
                        }
                        // TextElement
                        if(getTreeItem() instanceof TextTreeItem){
                            ((TextTreeItem) getTreeItem()).updateCell(this);
                            return;
                        }

                        // Other
                        setStyle(null);
                        setGraphic(null);
                        setContextMenu(null);
                        setOnMouseClicked(null);

                    }
                };
            }
        });

    }

    public static ContextMenu getCategoryMenu(boolean favorites){


        ContextMenu menu = new ContextMenu();
        NodeMenuItem item1 = new NodeMenuItem(new HBox(), TR.tr("Vider la liste"), -1, false);
        item1.setToolTip(TR.tr("Supprime tous les éléments de la liste. Ne supprime en aucun cas les éléments sur le document."));
        NodeMenuItem item2 = new NodeMenuItem(new HBox(), TR.tr("Supprimer les donnés d'utilisation"), -1, false);
        item2.setToolTip(TR.tr("Réinitialise les donnés des éléments de la liste indiquant le nombre d'utilisation de l'élément. Cela va réinitialiser l'ordre du tri par Utilisation."));

        menu.getItems().addAll(item1, item2);
        Builders.setMenuSize(menu);

        item1.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(favorites) Main.lbTextTab.clearSavedFavoritesElements();
                else Main.lbTextTab.clearSavedLastsElements();
            }
        });
        item2.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(favorites){
                    for(TreeItem<String> element : Main.lbTextTab.favoritesText.getChildren()){
                        if(element instanceof TextTreeItem){
                            ((TextTreeItem) element).setUses(0);
                        }
                    }
                    if(Main.lbTextTab.favoritesTextSortManager.getSelectedButton().getText().equals(TR.tr("Utilisation"))){
                        Main.lbTextTab.favoritesTextSortManager.simulateCall();
                    }
                }else{
                    for(TreeItem<String> element : Main.lbTextTab.lastsText.getChildren()){
                        if(element instanceof TextTreeItem){
                            ((TextTreeItem) element).setUses(0);
                        }
                    }
                    if(Main.lbTextTab.lastsTextSortManager.getSelectedButton().getText().equals(TR.tr("Utilisation"))){
                        Main.lbTextTab.lastsTextSortManager.simulateCall();
                    }
                }
            }
        });

        return menu;
    }

    public static ContextMenu getNewMenu(TextTreeItem element){

        ContextMenu menu = new ContextMenu();
        NodeMenuItem item1 = new NodeMenuItem(new HBox(), TR.tr("Ajouter"), -1, false);
        item1.setToolTip(TR.tr("Ajoute cet élément à l'édition du document ouvert."));
        NodeMenuItem item2 = new NodeMenuItem(new HBox(), TR.tr("Retirer"), -1, false);
        item2.setToolTip(TR.tr("Retire cet élément de la liste. Si l'élément est lié, l'élément lié ne sera supprimé que si vous êtes dans la catégorie des éléments sur ce document."));
        NodeMenuItem item3 = new NodeMenuItem(new HBox(), TR.tr("Ajouter aux favoris"), -1, false);
        item3.setToolTip(TR.tr("Ajoute cet élément à la liste des éléments précédents."));
        NodeMenuItem item4 = new NodeMenuItem(new HBox(), TR.tr("Ajouter aux éléments précédents"), -1, false);
        item4.setToolTip(TR.tr("Ajoute cet élément à la liste des éléments favoris."));
        NodeMenuItem item5 = new NodeMenuItem(new HBox(), TR.tr("Dé-lier l'élément"), -1, false);
        item5.setToolTip(TR.tr("Dé-lie l'élément : l'élément de sera plus synchronisé avec l'élément du document."));


        // Ajouter les items en fonction du type
        menu.getItems().addAll(item1, item2);
        if(element.getType() != TextTreeItem.FAVORITE_TYPE) menu.getItems().add(item3); // onFile & lasts
        if(element.getType() == TextTreeItem.ONFILE_TYPE) menu.getItems().add(item4); // onFile
        if(element.getType() == TextTreeItem.LAST_TYPE && element.getCore() != null) menu.getItems().add(item5); // élément précédent qui est lié

        Builders.setMenuSize(menu);

        // Définis les actions des boutons
        item1.setOnAction((e) -> {
            element.addToDocument();
            if(element.getType() == TextTreeItem.FAVORITE_TYPE){
                Main.lbTextTab.favoritesTextSortManager.simulateCall();
            }else if(element.getType() == TextTreeItem.LAST_TYPE){
                Main.lbTextTab.lastsTextSortManager.simulateCall();
            }
        });
        item2.setOnAction((e) -> {
            if(element.getType() == TextTreeItem.ONFILE_TYPE){
                element.getCore().delete();
            }else{
                Main.lbTextTab.removeSavedElement(element);
            }
        });
        item3.setOnAction((e) -> {
            Main.lbTextTab.addSavedElement(new TextTreeItem(element.getFont(), element.getText(), element.getColor(), TextTreeItem.FAVORITE_TYPE, 0, System.currentTimeMillis()/1000));
            if(element.getType() == TextTreeItem.LAST_TYPE){
                if(Main.settings.isRemoveElementInPreviousListWhenAddingToFavorites()){
                    Main.lbTextTab.removeSavedElement(element);
                }
            }
        });
        item4.setOnAction((e) -> {
            Main.lbTextTab.addSavedElement(new TextTreeItem(element.getFont(), element.getText(), element.getColor(), TextTreeItem.LAST_TYPE, 0, System.currentTimeMillis()/1000));
        });
        item5.setOnAction((e) -> {
            element.unLink();
        });
        return menu;

    }

}



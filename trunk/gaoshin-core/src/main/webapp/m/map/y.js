YAHOO.namespace("gogomapper.container");

function onMenuBarReady () 
{
    // Animation object
    var oAnim;

    function setupMenuAnimation(p_oMenu) 
    {
        if(!p_oMenu.animationSetup) {
            var aItems = p_oMenu.getItemGroups();
            if(aItems && aItems[0]) {
                var i = aItems[0].length - 1;
                var oSubmenu;

                do {
                    oSubmenu = p_oMenu.getItem(i).cfg.getProperty("submenu");
                    if(oSubmenu) {
                        oSubmenu.beforeShowEvent.subscribe(onMenuBeforeShow, oSubmenu, true);
                        oSubmenu.showEvent.subscribe(onMenuShow, oSubmenu, true);
                    }
                }
                while(i--);
            }
            p_oMenu.animationSetup = true;
        }
    }

    function onMenuBeforeShow(p_sType, p_sArgs, p_oMenu) {
        if(oAnim && oAnim.isAnimated()) {
            oAnim.stop();
            oAnim = null;
        }

        YAHOO.util.Dom.setStyle(this.element, "overflow", "hidden");
        YAHOO.util.Dom.setStyle(this.body, "marginTop", ("-" + this.body.offsetHeight + "px"));
    }


    // "show" event handler for each submenu of the menu bar

    function onMenuShow(p_sType, p_sArgs, p_oMenu) {

        oAnim = new YAHOO.util.Anim(
            this.body, 
            { marginTop: { to: 0 } },
            .5, 
            YAHOO.util.Easing.easeOut
        );

        oAnim.animate();
        var me = this;
            
        function onTween() {
            me.cfg.refireEvent("iframe");
        }

        function onAnimationComplete() {
            YAHOO.util.Dom.setStyle(me.body, "marginTop", ("0px"));
            YAHOO.util.Dom.setStyle(me.element, "overflow", "visible");
            setupMenuAnimation(me);
        }

        if(this.cfg.getProperty("iframe") == true) {
            oAnim.onTween.subscribe(onTween);
        }
        oAnim.onComplete.subscribe(onAnimationComplete);
    }

    function onMenuRender(p_sType, p_sArgs, p_oMenu) {
        setupMenuAnimation(p_oMenu);
    }
    
//        var oMenuBar = new YAHOO.widget.MenuBar("gogomapperMenu", { autosubmenudisplay:true, hidedelay:750, lazyload:true });
//        oMenuBar.renderEvent.subscribe(onMenuRender, oMenuBar, true);
//        oMenuBar.render();
//        document.getElementById("menuWrapper").style.display = "block";



            var oMenu = new YAHOO.widget.Menu(
                                "productsandservices", 
                                {
                                    position:"static", 
                                    hidedelay:750, 
                                    lazyload:true 
                                }
                            );

            oMenu.render();
    


    
}
        

function createContextMenu() {
    var aItemData = [
    	{text: "Start A New Place Here", onclick:{fn:startNewFileXY, obj:["abc"]}}
    	, {text: "Start A New Route Here", onclick:{fn:startNewRouteXY, obj:["abc"]}}
    	, {text: "Change Caption", onclick:{fn:changeCaption}}
    	, {text: "Head-Tail Connected", onclick:{fn:toggleShape}, checked:false}
    	, {text: "Delete This Point", onclick:{fn:deleteCurrentPoint}}
    	, {text: "Delete Current Route", onclick:{fn:deleteCurrentRoute}}
    	, {text: "Delete Current File (Routes in Red)", onclick:{fn:deleteCurrentFile}}
    	/*, { text:"Edit", submenu:{ id: "editmenu", itemdata: [
                [{ text:"Undo", helptext: "Ctrl + Z" },
                 { text:"Redo", helptext: "Ctrl + Y", disabled: true }
                ],
                [{ text:"Cut", helptext: "Ctrl + X", disabled: true },
                 { text:"Delete", helptext: "Del", disabled: true }
                ],
                [ { text:"Select All", helptext: "Ctrl + A" } ],
                [{ text:"Find", helptext: "Ctrl + F" },
                 { text:"Find Again", helptext: "Ctrl + G" }
                ]
            ] }} */
    ];
    
    var oMenu = new YAHOO.widget.Menu("mymenubar", {clicktohidden:true, autosubmenudisplay:true});
    oMenu.addItems(aItemData);
    oMenu.render(document.body);
    //oMenu.cfg.setProperty("x", 200);
    //oMenu.cfg.setProperty("y", 400);
    //oMenu.show();
    
    gView.menu = oMenu;
}

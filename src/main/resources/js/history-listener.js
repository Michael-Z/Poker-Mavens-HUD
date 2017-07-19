// wrap this up in en immediately executed function so we don't
// junk up our global scope.
(function() {


    var clientFrame = document.querySelector('#client_frame.ap');
    var clientDocument = clientFrame.contentDocument;
    var historyInfo = clientFrame.contentDocument.querySelector('.historyinfo .memo.noselect')

   // create an observer instance
   var observer = new WebKitMutationObserver(function(mutations) {
       window.hud.handle(historyInfo.innerText)
   });

   // configuration of the observer:
   var config = { childList: true };

   // pass in the target node, as well as the observer options
   observer.observe(historyInfo, config);



})();
/**
 * li検索ボックス
 * 
 * 利用側のhtmlで下記の四つを定義する。
 * var queryBoxName = 'query';
 * var listName = '#names';
 * var countName = '#count';
 * var noneName = '#noneName';
 * 
 * 元ネタ検索中...
 */

(function($) {
    $(document).ready(function() {
      $('input[name="'+ queryBoxName +'"]').search(listName + ' li', function(on) {
        on.all(function(results) {
          var size = results ? results.size() : 0;
          $(countName).text(size + ' results');
        });

        on.reset(function() {$(noneName).hide(); $(listName+' li').show(); });
        on.empty(function() {$(noneName).show(); $(listName+' li').hide(); });
        on.results(function(results) {$(noneName).hide(); $(listName+' li').hide(); results.show(); });
      });
    });
  })(jQuery);

(function($) {
	  $.extend($.expr[':'], {
	    'containsi': function(elem, i, match, array) {
	      return $(elem).text().toLowerCase()
	        .indexOf((match[3] || "").toLowerCase()) >= 0;
	    }
	  });

	  var Search = function(block) {
	    this.callbacks = {};
	    block(this);
	  }

	  Search.prototype.all = function(fn) { this.callbacks.all = fn; }
	  Search.prototype.reset = function(fn) { this.callbacks.reset = fn; }
	  Search.prototype.empty = function(fn) { this.callbacks.empty = fn; }
	  Search.prototype.results = function(fn) { this.callbacks.results = fn; }

	  function query(selector) {
	    if (val = this.val()) {
	      return $(selector + ':containsi("' + val + '")');
	    } else {
	      return false;
	    }
	  }

	  $.fn.search = function search(selector, block) {
	    var search = new Search(block);
	    var callbacks = search.callbacks;

	    function perform() {
	      if (result = query.call($(this), selector)) {
	        callbacks.all && callbacks.all.call(this, result);
	        var method = result.size() > 0 ? 'results' : 'empty';
	        return callbacks[method] && callbacks[method].call(this, result);
	      } else {
	        callbacks.all && callbacks.all.call(this, $(selector));
	        return callbacks.reset && callbacks.reset.call(this);
	      };
	    }

	    $(this).on('keypress',  perform);
	    $(this).on('keydown',  perform);
	    $(this).on('keyup',  perform);
	    $(this).on('blur', perform);
	  }
	})(jQuery);

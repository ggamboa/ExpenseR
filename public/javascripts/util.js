function CurrencyFormatted(amount) {
	var i = parseFloat(amount);
	if(isNaN(i)) { i = 0.00; }
	var minus = '';
	if(i < 0) { minus = '-'; }
	i = Math.abs(i);
	i = parseInt((i + .005) * 100);
	i = i / 100;
	s = new String(i);
	if(s.indexOf('.') < 0) { s += '.00'; }
	if(s.indexOf('.') == (s.length - 2)) { s += '0'; }
	s = minus + s;
	return s;
}

function formatAmounts() {
	formatTotalAmount();
	formatSubTotalAmount();
}

function formatTotalAmount() {
	var x = document.getElementsByClassName("totalcell");
	var text = "";

	for (i = 0; i < x.length; i++) {
		
		var content = x[i].innerHTML.split(":");
		//var content = x[i].innerHTML;
		var n = parseFloat(content[1]);
		var nfixed = n.toFixed(2);
		//var nfixed = CurrencyFormatted(n);

   		text = content[0] + ": $" + nfixed.toString();
   		//text = nfixed.toString();
   		
   		x[i].innerHTML = text;	
	}	
}

function formatSubTotalAmount() {
	var x = document.getElementsByClassName("subtotalcell");
	var text = "";

	for (i = 0; i < x.length; i++) {
		
		var content = x[i].innerHTML.split(":");
		//var content = x[i].innerHTML;
		var n = parseFloat(content[1]);
		var nfixed = n.toFixed(2);
		//var nfixed = CurrencyFormatted(n);

   		//text = content[0] + ": " + nfixed.toString();
   		text = nfixed.toString();
   		
   		x[i].innerHTML = text;	
	}	
}

//ver 1.1

  function handleFileSelect(evt) {
    var files = evt.target.files; // FileList object

    // files is a FileList of File objects. List some properties.
    var output = [];
    for (var i = 0, f; f = files[i]; i++) {
      output.push('<li><strong>', escape(f.name), '</strong> (', f.type || 'n/a', ') - ',
                  f.size, ' bytes, last modified: ',
                  f.lastModifiedDate ? f.lastModifiedDate.toLocaleDateString() : 'n/a',
                  '</li>');
    }
    document.getElementById('list').innerHTML = '<ul>' + output.join('') + '</ul>';
  }

  document.getElementById('files').addEventListener('change', handleFileSelect, false);




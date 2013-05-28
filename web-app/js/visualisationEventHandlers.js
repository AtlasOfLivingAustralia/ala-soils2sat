function plantSpeciesBreakDownBySourceClick(chart, link) {

    if (chart.getSelection().length > 0) {
        showModal({
            url: link + "&row=" + chart.getSelection()[0].row,
            title: "Please wait..."
        });
    }

}

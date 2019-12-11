google.charts.load('current', {'packages':['gantt']});
google.charts.setOnLoadCallback(drawChart);








    function drawChart() {

    var myVar = Android.getData();


    var taskList = JSON.parse(myVar);

    var datas = [];

    Android.showToast()


      var data = new google.visualization.DataTable();
      data.addColumn('string', 'Task ID');
      data.addColumn('string', 'Task Name');
      data.addColumn('string', 'Resource');
      data.addColumn('date', 'Start Date');
      data.addColumn('date', 'End Date');
      data.addColumn('number', 'Duration');
      data.addColumn('number', 'Percent Complete');
      data.addColumn('string', 'Dependencies');

      taskList.forEach(task=>{
      let progress = task.volume_of_work_done*100/task.volume_of_works;

      console.log(taskList)

      let duration = null;

//      if(task.task_finished_date-task.task_start_date<24*60*60*1000){
//        task.task_finished_date = task.task_start_date+24*60*60*1000;
//      }

      datas.push([task._id,task.name,task._id,new Date(task.start_date),
                         new Date(task.finished_date),null,progress,null])
      })


      data.addRows(datas);


//      2014Spring

      /*data.addRows([
        ['2014Spring', taskList[0].unit, 'spring',
         new Date(2014, 2, 22), new Date(2014, 5, 20), null, 100, null],
        ['2014Summer', 'Summer 2014', 'summer',
         new Date(2014, 5, 21), new Date(2014, 8, 20), null, 100, null],
        ['2014Autumn', 'Autumn 2014', 'autumn',
         new Date(2014, 8, 21), new Date(2014, 11, 20), null, 100, null],
        ['2014Winter', 'Winter 2014', 'winter',
         new Date(2014, 11, 21), new Date(2015, 2, 21), null, 100, null],
        ['2015Spring', 'Spring 2015', 'spring',
         new Date(2015, 2, 22), new Date(2015, 5, 20), null, 50, null],
        ['2015Summer', 'Summer 2015', 'summer',
         new Date(2015, 5, 21), new Date(2015, 8, 20), null, 0, null],
        ['2015Autumn', 'Autumn 2015', 'autumn',
         new Date(2015, 8, 21), new Date(2015, 11, 20), null, 0, null],
        ['2015Winter', 'Winter 2015', 'winter',
         new Date(2015, 11, 21), new Date(2016, 2, 21), null, 0, null],
        ['Football', 'Football Season', 'sports',
         new Date(2014, 8, 4), new Date(2015, 1, 1), null, 100, null],
        ['Baseball', 'Baseball Season', 'sports',
         new Date(2015, 2, 31), new Date(2015, 9, 20), null, 14, null],
        ['Basketball', 'Basketball Season', 'sports',
         new Date(2014, 9, 28), new Date(2015, 5, 20), null, 86, null],
        ['Hockey', 'Hockey Season', 'sports',
         new Date(2014, 9, 8), new Date(2015, 5, 21), null, 89, null]
      ]);*/

      var options = {
        height: taskList.length*30,

        gantt: {
              criticalPathEnabled: false, // Critical path arrows will be the same as other arrows.
              arrow: {
                angle: 50,
                width: 1,
                color: 'white',
                radius: 2
              },

              palette:[

                {
                  "color": "#5c6bc0",
                  "dark": "#3949ab",
                  "light": "#c5cae9"
                }
              ],
              labelStyle: {
                fontName: 'Open Sans',
                fontSize: 14,
                color: '#ff0000'
              },
              barCornerRadius: 2,
              backgroundColor: {
                fill: 'transparent',
              },
              innerGridHorizLine: {
                stroke: '#ddd',
                strokeWidth: 0,
              },
              innerGridTrack: {
                fill: 'transparent'
              },
              innerGridDarkTrack: {
                fill: 'transparent'
              },
              percentEnabled:	true,
              shadowEnabled: true,
              shadowColor: 'white',
              shadowOffset: 2,

              trackHeight: 30
            }

      };

      var chart = new google.visualization.Gantt(document.getElementById('chart_div'));
      google.visualization.events.addListener(chart, 'ready', afterDraw);

      chart.draw(data, options);
    }


    function afterDraw() {
        var g = document.getElementsByTagName("svg")[0].getElementsByTagName("g")[1];
        document.getElementsByTagName("svg")[0].parentNode.style.top = '40px';
        document.getElementsByTagName("svg")[0].style.overflow = 'visible';
        var height = Number(g.getElementsByTagName("text")[0].getAttribute('y')) + 15;
        g.setAttribute('transform','translate(0,-'+height+')');
        g = null;

        Android.showToast();
    }
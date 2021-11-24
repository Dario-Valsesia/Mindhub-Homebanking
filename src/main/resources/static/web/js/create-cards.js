const app = Vue.createApp({
    data(){
        return{
            darkMode:false,
            inputType:"",
            inputColor:"",
            client:[],
            errorCreate:"",

        }
    },
    created(){
        this.darkMode = JSON.parse(localStorage.getItem('dark'));
        this.loadClient()
    },
    methods:{
        loadClient(){
            axios.get('/api/clients/current')
                .then(res=>this.client=res.data)
        },
        setDarkMode(e){
            this.darkMode = !this.darkMode;
            localStorage.setItem("dark", JSON.stringify(this.darkMode));
            document.querySelector('.btn-nav-close').click();
        },
        createCard(e){
            if(this.inputType !="" && this.inputColor !=""){
                axios.post(`/api/clients/current/cards`,`type=${this.inputType}&color=${this.inputColor}`).then(res=>window.location.href="./cards.html").catch(e=>{
                    this.errorCreate=e.response.data;
                });

            }else{
                this.errorCreate="Select color"
            }
            
        },
        rotateCreateCard(e){

            if(e.target.type==undefined){
                this.inputColor="";
                this.errorCreate="";
            }
            if(e.target.type=="submit"){
                this.inputType = e.target.value
            }            
            e.target.closest('.containerCreateCard').classList.toggle('rotateCreateCard');
        },
        cardColor(e){
            this.inputColor=e.target.value
        },
        rotateCard(e){
              e.target.closest('.card').classList.toggle('toTurnJS');
        },
        date(){
            let today = new Date();
            let day =  today.getDate();
            let month =  today.getMonth() + 1;
            let year =  today.getFullYear();
            let format = `${month}/${year}`
            return `${format.slice(0,4)}${format.slice(-1)}`;
        }
    },

})

let mount = app.mount('#app');
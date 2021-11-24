const app = Vue.createApp({
    data(){
        return{
            darkMode:false,
            originAccount:true,
            destination:false,
            yourAccount:false,
            numberAccount:false,
            yourAmount:false,
            description:false,
            accounts:[],
            accountSelect:"",
            accountDestiny:"",
            inputAmount:0,
            inputDescription:"",
            confirm:false,
            messageError:"",
            successful:false,
            
        }
    },
    created(){
        this.darkMode = JSON.parse(localStorage.getItem('dark'));
        this.loadData();
    },
    methods:{
    setDarkMode(e){
            this.darkMode = !this.darkMode;
            localStorage.setItem("dark", JSON.stringify(this.darkMode));
            document.querySelector('.btn-nav-close').click();
    },
    loadData(){
        axios.get('/api/clients/current').then(res=>{
            this.accounts = res.data.accounts;
        })
    },
    createTransaction(){
        if(this.inputAmount===""){
            this.inputAmount=0;
        }
        axios.post('/api/transactions',`amount=${this.inputAmount}&numberAccount=${this.accountSelect}&numberAccountDestiny=${this.accountDestiny}&description=${this.inputDescription}`).then(res=>this.successful=true).catch(e=>{
            this.messageError = e.response.data;
        });
    },

    //METODOS NEXT
    destination1(e){
        this.accountSelect=e.target.value;
        let element = e.target.closest('.d-flex');
        element.classList.add('exit');
        this.destination=true;
        setTimeout(()=>{
            this.originAccount=false;
            element.classList.remove('exit');
        },1100)
    },
    ownAccount(e){
        let element = e.target.closest('.d-flex')
        element.classList.add('exit');
        this.yourAccount=true;
        setTimeout(()=>{
            this.destination=false;
            element.classList.remove('exit');
        },1100)
    },
    third(e){
        let element = e.target.closest('.d-flex')
        element.classList.add('exit');
        this.numberAccount=true;
        setTimeout(()=>{
            this.destination=false;
            element.classList.remove('exit');
        },1100)
    },
    amount(e){
        if(e.target.value!=""){
            this.accountDestiny=e.target.value;
        }
        let element = e.target.closest('.d-flex')
        element.classList.add('exit');
        this.yourAmount=true;
        setTimeout(()=>{
            this.numberAccount=false;
            this.yourAccount=false;
            element.classList.remove('exit');
        },1100)
    },
    descri(e){
        let element = e.target.closest('.d-flex')
        element.classList.add('exit');
        this.description=true;
        setTimeout(()=>{
            this.yourAmount=false;
            element.classList.remove('exit');
        },1100)
    },
    confirmBtn(){
        this.confirm=true;
    },

    
    //METODOS BACK
    cancel(){
        this.confirm=false;
        this.description=false;
        this.originAccount=true;
        this.accountSelect='';
        this.accountDestiny='';
        this.inputAmount=0;
        this.inputDescription='';
        this.messageError='';
    },
    backAmount(e){
        this.confirm=false;
        this.messageError='';
        let element = e.target.closest('.d-flex')
        element.classList.add('exitBack');
        this.yourAmount=true;
        setTimeout(()=>{
            this.description=false;
            element.classList.remove('exitBack');
        },1100)
    },
    backNumber(e){
        let element = e.target.closest('.d-flex')
        element.classList.add('exitBack');
        this.numberAccount=true;
        setTimeout(()=>{
            this.yourAmount=false;
            element.classList.remove('exitBack');
        },1100)
    },
    backDestination(e){
        let element = e.target.closest('.d-flex')
        element.classList.add('exitBack');
        this.destination=true;
        setTimeout(()=>{
            this.numberAccount=false;
            this.yourAccount=false;
            element.classList.remove('exitBack');
        },1100)
    },
    backOrigin(e){
        let element = e.target.closest('.d-flex')
        element.classList.add('exitBack');
        this.originAccount=true;
        setTimeout(()=>{
            this.destination=false;
            element.classList.remove('exitBack');
        },1100)

    },
    //MODAL
    closeModal(){
        this.successful=false;
        location.reload();
    },
    downloadPdf(){
           axios.post('/api/pdf/generate', `accountOrigin=${this.accountSelect}&accountDestiny=${this.accountDestiny}&amount=${this.inputAmount}&description=${this.inputDescription}`,{responseType: 'blob'})
            .then(res=>{               
                let disposition = res.headers['content-disposition'];
                let fileName = decodeURI(disposition.substring(21));
                /* const blob = new  Blob ([res.data], {
                    type : 'application/pdf',
                }); */
                let link = document.createElement('a')
                link.href = window.URL.createObjectURL(res.data)
                link.download = fileName
                link.click()
                link.remove()
                setTimeout(()=>{
                    location.reload();
                },1000)           
            }).catch(e=>e);
    }
    },
    computed:{
        accountFilter(){
        return this.accounts.filter(account=>account.number!=this.accountSelect);
        }
    }
})

let mount = app.mount('#app');
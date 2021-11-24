const app = Vue.createApp({
   data(){
       return{
           darkMode:false,
           accounts:[],
           loans:[],
           //VIEW
           viewAccount:true,
           viewLoan:false,
           viewAmount:false,
           viewPaymets:false,
           viewConfirm:false,
           viewModal:false,
           //Parameters request post
           selectAccount:"",
           selectLoan:"",
           selectAmount:0,
           selectPayment:0,
           //Message error
           requestMessage:"",
           errorMessage:"",
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
            }).catch(e=>e);
            axios.get('/api/loans').then(res=>{
                this.loans=res.data
            }).catch(e=>e);

        },
        createLoan(){
            axios.post('/api/loans',{
                "name":this.selectLoan,
                "amount":this.selectAmount,
                "payments":this.selectPayment,
                "numberAccount":this.selectAccount
            }).then(res=>{
                if(res.status==201){
                    this.requestMessage="Loan made correctly"
                }
                setTimeout(()=>{
                    location.reload();
                },2000)
            }).catch(e=>this.errorMessage=e.response.data)
        },
        //Methods view
        nextAccount(e){
            this.selectAccount=e.target.value;
            let element = e.target.closest('.containerOptionLoan');
            element.classList.add('exitLoan');
            this.viewLoan=true;
            setTimeout(()=>{
                this.viewAccount=false;
                element.classList.remove('exitLoan');
            },1100)
        },
        nextLoan(e){
            this.selectLoan=e.target.value
            let element = e.target.closest('.containerOptionLoan');
            element.classList.add('exitLoan')
            this.viewAmount=true;
            setTimeout(()=>{
                this.viewLoan=false;
                element.classList.remove('exitLoan');
            },1100)
        },
        nextAmount(e){
            let element = e.target.closest('.containerOptionLoan');
            element.classList.add('exitLoan')
            this.viewPaymets=true;
            setTimeout(()=>{
                this.viewAmount=false;
                element.classList.remove('exitLoan');
            },1100)
        },
        nextPayment(e){
            this.selectPayment=e.target.value;
            let element = e.target.closest('.containerOptionLoan');
            element.classList.add('exitLoan')
            this.viewConfirm=true;
            setTimeout(()=>{
                this.viewPaymets=false;
                element.classList.remove('exitLoan');
            },1100)
        },
        nextConfirm(){
            this.viewModal=true;
        },
        closeModal(){   
            this.viewModal=false;
            location.reload();
        },
        //BACK METHODS
        backLoan(e){
            let element = e.target.closest('.containerOptionLoan');
            element.classList.add('backLoan')
            this.viewAccount=true;
            setTimeout(()=>{
                this.viewLoan=false;
                element.classList.remove('backLoan');
            },1100)
        },
        backAmount(e){
            this.selectAmount=0;
            let element = e.target.closest('.containerOptionLoan');
            element.classList.add('backLoan')
            this.viewLoan=true;
            setTimeout(()=>{
                this.viewAmount=false;
                element.classList.remove('backLoan');
            },1100)
        },
        backPayment(e){
            let element = e.target.closest('.containerOptionLoan');
            element.classList.add('backLoan')
            this.viewAmount=true;
            setTimeout(()=>{
                this.viewPaymets=false;
                element.classList.remove('backLoan');
            },1100)
        },
        backConfirm(e){
            let element = e.target.closest('.containerOptionLoan');
            element.classList.add('backLoan')
            this.viewPaymets=true;
            setTimeout(()=>{
                this.viewConfirm=false;
                element.classList.remove('backLoan');
            },1100)
        }

   },
   computed:{
       option(){
           let loan = this.loans.filter(loan=>loan.name==this.selectLoan);
           return loan[0].payments;
       }
   }
})

let mount = app.mount('#app');
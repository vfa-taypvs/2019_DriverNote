//
//  ViewController.swift
//  CamNangNguoiLaiXe
//
//  Created by Hung Le on 3/4/17.
//  Copyright © 2017 Hung Le. All rights reserved.
//

import UIKit
import EZSwiftExtensions
import AlamofireObjectMapper
import ObjectMapper
import Alamofire
import FacebookLogin
import FacebookShare
import FacebookCore

class ViewController: BaseViewController, LoginButtonDelegate {
    
    let documentsDirectoryPathString = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true).first!
    let ai = UIActivityIndicatorView(activityIndicatorStyle: .whiteLarge)
    let itemHeight: CGFloat = 40
    let itemHeightPadding: CGFloat = 2
    var imgBg : UIImageView?
    var arrData = [ModelData]()
    var scrollView: UIScrollView?
    
    override func viewDidLoad() {
        page = "Home";
        super.viewDidLoad()
        checkRotated()
        lblTitleHeader?.text = "Cẩm nang người lái xe"
        lblTitleHeader?.font = UIFont(name: "vni_common_VBODONP" , size: 50)
        self.view.removeSubviews()
        ai.frame = CGRect(x: 0, y: 0, w: 70, h: 70)
        ai.backgroundColor = UIColor(white: 0.0, alpha: 0.7)
        ai.layer.cornerRadius = 10
        ai.center = self.view.center
        self.view.addSubview(ai)

        print ("Scene : Main View Controller")
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(true)
        currentOrientation = ""
        if viewHeader != nil {
            checkRotated()
        }
    }
    
    func showShareDialog<C: ContentProtocol>(_ content: C, mode: ShareDialogMode = .automatic) {
        let dialog = ShareDialog(content: content)
        dialog.presentingViewController = self
        dialog.mode = mode
        do {
            try dialog.show()
        } catch {
            
        }
    }
    
    func loginManagerDidComplete(_ result: LoginResult) {
        let alertController: UIAlertController
        switch result {
        case .cancelled:
            print("Canceled")
//            alertController = UIAlertController(title: "Login Cancelled", message: "User cancelled login.")
        case .failed(let error):
            print("Failed")
//            alertController = UIAlertController(title: "Login Fail", message: "Login failed with error \(error)")
        case .success(let grantedPermissions, _, _):
            print("Success")
//            alertController = UIAlertController(title: "Login Success",
//                                                message: "Login succeeded with granted permissions: \(grantedPermissions)")
        }
//        self.present(alertController, animated: true, completion: nil)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    // MARK: - Get data
    func getData() {
        print("TayPVS - Get Data")
        if Reachability.isConnectedToNetwork() == true {
            // have internet
            ai.startAnimating()
            let url: String = "http://camnangnguoilaixe.com/public/api?cmd=get_all"
            Alamofire.request(url, method: .post).responseArray { (response: DataResponse<[ModelData]>) in
                self.ai.stopAnimating()
                if response.result.isSuccess {
                    self.arrData.removeAll()
                    self.arrData = response.result.value!
                    SessionToken.sharedInstance.globalData = self.arrData
                    
                    // Add Facebook
                    var facebookTitle:String = "Chia sẻ app trên Facebook"
                    var facebookType:String = "facebook"
                    let facebookRow = ModelData ()
                    facebookRow.setName(nameSet: facebookTitle)
                    facebookRow.setType(typeSet: facebookType)
                    self.arrData.insert(facebookRow, at: 0)
                    print("TayPVS - Get Data Success")
//                    self.checkRotated ()
                    self.saveDataInLocal()
                    self.setup (numOfColumn: 1)
                } else {
                    self.getDataLocal()
                }
            }
        } else {
            getDataLocal()
        }
        
        
    }
    
    func renderBodyList () {
        if (arrData.count == 0) {
            getData()
        } else {
            self.setup (numOfColumn: 1)
        }
    }
    
    func loginButtonDidCompleteLogin(_ loginButton: LoginButton, result: LoginResult) {
        print("Did complete login via LoginButton with result \(result)")
    }
    
    func loginButtonDidLogOut(_ loginButton: LoginButton) {
        print("Did logout via LoginButton")
    }
    
    func getDataLocal() {
        // load local
        let paths = NSSearchPathForDirectoriesInDomains(FileManager.SearchPathDirectory.documentDirectory, FileManager.SearchPathDomainMask.userDomainMask, true).first
//        let documentsDir = paths[0]
        let databasePath = paths! + "/init.json"
        print("TayPVS database path get local: \(paths)")
        let data = try! Data(contentsOf: URL(fileURLWithPath: databasePath))
        let json = try! JSONSerialization.jsonObject(with: data, options: []) as! [[String: Any]]
        let resultList: Array<ModelData>? = Mapper<ModelData>().mapArray(JSONArray: json)
        self.arrData.removeAll()
        for item in resultList! {
            self.arrData.append(item)
        }
        SessionToken.sharedInstance.globalData = self.arrData
//        setup(numOfColumn: 2)
        
        // Add Facebook
//        var facebookTitle:String = "Chia sẻ app trên Facebook"
//        var facebookType:String = "facebook"
//        let facebookRow = ModelData ()
//        facebookRow.setName(nameSet: facebookTitle)
//        facebookRow.setType(typeSet: facebookType)
//        arrData.insert(facebookRow, at: 0)
        
//        checkRotated ()
        setup (numOfColumn: 1)
    }
    
    func saveDataInLocal() {
        DispatchQueue.main.async {
            // creating JSON out of the above array
            var jsonData: NSData!
            do {
                jsonData = try JSONSerialization.data(withJSONObject: self.arrData.toJSON(), options: .prettyPrinted) as NSData?
            } catch let error as NSError {
                print("Array to JSON conversion failed: \(error.localizedDescription)")
            }
            
            let paths = NSSearchPathForDirectoriesInDomains(FileManager.SearchPathDirectory.documentDirectory, FileManager.SearchPathDomainMask.userDomainMask, true).first
//            let documentsDir = paths[0]
            let databasePath = paths! + "/init.json"
            let jsonFilePath = URL(fileURLWithPath: databasePath)
            do {
//                let file = try FileHandle(forWritingTo: jsonFilePath)
                jsonData.write(to: jsonFilePath, atomically: true)
//                file.write(jsonData as Data)
            } catch let error as NSError {
                print("Couldn't write to file: \(error.localizedDescription)")
            }
        }
        
    }

    // MARK: - Setup ui
    func setup(numOfColumn: CGFloat) {
        print("TayPVS - setup - renderPortrait ___________")
        clearAllSubview ()
        let navigationBarHeight: CGFloat = self.navigationController!.navigationBar.frame.height
        scrollView = UIScrollView(frame: CGRect(x: 0, y: (viewHeader?.h)! + 5, w: self.view.w, h: self.view.h - ((viewHeader?.h)! + 50 + 5 + navigationBarHeight)))
        scrollView?.isHidden = true
        scrollView?.backgroundColor = .white
        scrollView?.showsVerticalScrollIndicator = false
        self.view.addSubview(scrollView!)
        
        let imgBg = UIImageView(frame: CGRect(x: 0, y: (viewHeader?.h)! / 2 + 12, w: self.view.w, h: self.view.h))
        //            img.image = UIImage(named: item.icon)
        imgBg.isHidden = true
        imgBg.image = UIImage(named: "bg.png")
        imgBg.contentMode = .scaleAspectFit
        self.view.addSubview(imgBg)
        
        var i: CGFloat = 0
        let width = self.view.w / numOfColumn - 5
        let height = itemHeight
        var hView: CGFloat = 1

        // Temperally remove
//        arrData.remove(at: 7)
        
        let arrIcon = ["icon01.png", "icon02.png", "icon03.png", "icon04.png", "icon05.png", "icon06.png", "icon07.png", "icon08.png", "icon09.png", "icon10.png"]
        
        var index = 0;
        var posColor = 0
        for item in arrData {
            print("TayPVS - index: \(index)")
            print("TayPVS - Title: \(item.name)")
            var padding: CGFloat = 0
            var rowIndex: CGFloat = 0;
            var paddingHeight: CGFloat = 0;
            padding = 2;
            paddingHeight = itemHeightPadding;
            rowIndex = (i / numOfColumn)
            let vBtn = UIView(frame: CGRect(x: i * width + padding * (i + 1), y: hView + paddingHeight, w: width, h: height))
            vBtn.setCornerRadius(radius: 1)
            i += 1
            if i.truncatingRemainder(dividingBy: numOfColumn) == 0{
                i = 0
                hView = vBtn.y + vBtn.h + 1
            }
            let img = UIImageView(frame: CGRect(x: 10, y: 2, w: 20, h: 20))
//            img.image = UIImage(named: item.icon)
            img.image = UIImage(named: arrIcon[index])
            img.contentMode = .scaleAspectFit
            vBtn.addSubview(img)
            
            let lbl = UILabel(frame: CGRect(x: img.w + 40 , y: 2 , w: (scrollView?.w)! - img.w, h: 20))
            lbl.textAlignment = .left
            lbl.textColor = .black
            lbl.text = item.name
            lbl.lineBreakMode = NSLineBreakMode.byWordWrapping
            lbl.numberOfLines = 0
            lbl.font = UIFont.boldSystemFont(ofSize: 15)
            vBtn.addSubview(lbl)
//            vBtn.backgroundColor = arrColor[posColor]
            scrollView?.addSubview(vBtn)
            posColor += 1
            vBtn.addTapGesture(action: { (tap) in
                vBtn.alpha = 0.5
                UIView.animate(withDuration: 0.35, animations: {
                    vBtn.alpha = 1
                    if item.type_name == "facebook" {
                        guard let url = URL(string: "https://camnangnguoilaixe.com/") else { return }
                        var content = LinkShareContent(url: url)
                        
                        // placeId is hardcoded here, see https://developers.facebook.com/docs/graph-api/using-graph-api/#search
                        // for building a place picker.
                        content.placeId = "166793820034304"
                        
                        self.showShareDialog(content, mode: .automatic)

                    } else if item.type_name == "motthongtin" {
                        let motVC = MotThongTinViewController()
                        if (item.small_topic?.count)! > 0 {
                            motVC.model = item.small_topic?[0]
                            self.navigationController?.pushViewController(motVC, animated: true)
                        }
                    } else if item.position == "5" {
                        let xuphatVC = XuphatViewController()
                        xuphatVC.model = item
                        self.navigationController?.pushViewController(xuphatVC, animated: true)
                    } else {
                        let baoHieuVC = BaoHieuViewController()
                        baoHieuVC.model = item
                        self.navigationController?.pushViewController(baoHieuVC, animated: true)
                    }
                    
                })
            })
            index += 1
        }
        visibleAfterLoad ()
        imgBg.isHidden = false
        scrollView?.contentSize = CGSize(width: (scrollView?.w)!, height: getScrollSizeHeight (numOfRow: Int(numOfColumn)))
        setupAds()
//        rerenderAds()
    }
    
    func getScrollSizeHeight (numOfRow : Int) -> CGFloat {
        var totalHeight: CGFloat = 0
        let row: Int = self.arrData.count / numOfRow
        if self.arrData.count % numOfRow == 0 {
            totalHeight = CGFloat(row) * (itemHeight + itemHeightPadding)
        } else {
            totalHeight = CGFloat(row) * (itemHeight + itemHeightPadding) + itemHeight
        }
        return totalHeight
    }
    
    func clearAllSubview () {
        scrollView?.removeSubviews()
    }
    
    override func renderLandscape () {
        print("TayPVS - renderLandscape ___________")
        renderBodyList ()
        print("TayPVS - Done SETUP ___________")
    }
    
    override func renderPortrait () {
        print("TayPVS - renderPortrait ___________")
        renderBodyList ()
        print("TayPVS - Done SETUP ___________")
    }
    
    func visibleAfterLoad () {
        print("TayPVS - visibleAfterLoad")
        scrollView?.isHidden = false
        visibleHeader ()
    }
}


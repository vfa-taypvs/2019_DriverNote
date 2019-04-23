//
//  XuphatViewController.swift
//  CamNangNguoiLaiXe
//
//  Created by Hung Le on 3/5/17.
//  Copyright © 2017 Hung Le. All rights reserved.
//

import UIKit

class XuphatViewController: BaseViewController, UITableViewDelegate, UITableViewDataSource {

    var mTableView: UITableView = {
        let table = UITableView(frame: CGRect.zero, style: .plain)
        table.showsVerticalScrollIndicator = false
        table.separatorStyle = .none
        table.backgroundColor = UIColor(hexString: "ECF1F2")
        return table
    }()
    
    var model: ModelData?
    var data: ModelData?
    //var dataLoiPhat: ModelData?
    let arr: [String] = ["NGHỊ ĐỊNH 46/2016/NĐ-CP", "Một số điều phạt thường gặp", "Thẩm quyền xử phạt","THÔNG TƯ 01/2016/TT-BCA"]
    
    override func viewDidLoad() {
        page = "XuPhat"
        titlePage = model?.name
        super.viewDidLoad()
        checkRotated()
        let JSONString = model?.toJSONString(prettyPrint: true)
        data = ModelData(JSONString: JSONString!)
        //dataLoiPhat = ModelData(JSONString: JSONString!)
        var position: NSInteger = 0
        
        //dataLoiPhat?.small_topic?.removeAll()
        for item in (data?.small_topic)! {
            
            if (item.title.contains ("46/2016/") || item.title == "THẨM QUYỀN XỬ PHẠT"
//                || item.title.contains("hành vi vi phạm")
                || item.title.contains("01/2016/TT")){
//                if item.title.contains("hành vi vi phạm") {
//                    dataLoiPhat?.small_topic?.append(item)
//                    print ("Hanh Vi Vi Pham : \(item.title)" )
//                }
                print ("TAYPVS - Topic Title REmove : \(item.title)" )
                data?.small_topic?.remove(at: position)
                position = 0
            } else {
                position += 1
            }
        }
        updateUI()
//        setup()
        print ("Scene : XU Phat")
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(true)
        currentOrientation = ""
        if viewHeader != nil {
            checkRotated()
        }
    }
    
    override func actionScrollTop() {
        mTableView.setContentOffset(CGPoint.zero, animated: true)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func setup() {
        mTableView.frame = CGRect(x: 0, y: (viewHeader?.h)!, w: self.view.w, h: self.view.h - (viewHeader?.h)!)
        mTableView.register(XuphatCell.self, forCellReuseIdentifier: "XuphatCell")
        mTableView.delegate = self
        mTableView.dataSource = self
        mTableView.contentInset = UIEdgeInsetsMake(-30, 0, 0, 0)
        view.addSubview(mTableView)
        setupAds()
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
//        if let small = model?.small_topic {
//            return small.count
//        } else {
//            return 0
//        }
        return arr.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 70
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        var h: CGFloat = 0
        if section == 0{
            h = 50
        } else {
            h = 30
        }
        let v = UIView(frame: CGRect(x: 0, y: 0, w: tableView.w, h: h))
        if section == 0 {
            v.backgroundColor = .clear
        } else {
            v.backgroundColor = UIColor(hexString: "ECF1F2")
        }
        return v
    }
    
//    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
//        if let small = model?.small_topic {
//            if section == small.count - 1 {
//                return 50
//            } else {
//                return 0
//            }
//        } else {
//            return 0
//        }
//    }
    
    func tableView(_ tableView: UITableView, viewForFooterInSection section: Int) -> UIView? {
        if let small = model?.small_topic {
            if section == small.count - 1 {
                let v = UIView(frame: CGRect(x: 0, y: 0, w: tableView.w, h: 50))
                v.backgroundColor = .clear
                return v
            } else {
                return UIView()
            }
        } else {
            return UIView()
        }
        
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        if section == 0{
            return 50
        } else {
            return 30
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "XuphatCell", for: indexPath) as! XuphatCell
        cell.backgroundColor = .clear
        cell.selectionStyle = .none
        cell.lbl?.text = arr[indexPath.section].uppercased()
        cell.updateData()
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        if indexPath.section == 0 {
            // pdf
            for item in (model?.small_topic)! {
                if item.type_name.lowercased() == "pdf" {
                    let pdfVC = PDFViewController()
                    pdfVC.model = item
                    self.navigationController?.pushViewController(pdfVC, animated: true)
                    break
                }
            }
            print ("0 - table")
//            for item in (model?.small_topic)! {
//                if item.type_name.lowercased() == "multi" && item.position == "1" {
//                    let motVC = MultiViewController()
//                    motVC.model = item
//                    self.navigationController?.pushViewController(motVC, animated: true)
//                    break
//                }
//            }
            
        } else if indexPath.section == 1 {
            print ("1 - table")
            let baoHieuVC = BaoHieuViewController()
            baoHieuVC.model = data
            self.navigationController?.pushViewController(baoHieuVC, animated: true)
        } else if  indexPath.section == 3 {
            print ("3 - table")
            for item in (model?.small_topic)! {
                print ("3 - table - " + item.type_name + " - position :" + item.position)
                if item.type_name.lowercased() == "text" && item.position == "3" {
                    let motVC = MotThongTinViewController()
                    motVC.model = item
                    self.navigationController?.pushViewController(motVC, animated: true)
                    break
                }
            }
        } else {
            print ("2 - table")
            for item in (model?.small_topic)! {
                if item.type_name.lowercased() == "text" && item.position == "1" {
                    let motVC = MotThongTinViewController()
                    motVC.model = item
                    self.navigationController?.pushViewController(motVC, animated: true)
                    break
                }
            }
            
        }
    }

    
    override func onAfterLoadHeader () {
        btnBack?.isHidden = false
        btnTop?.isHidden = false
    }
    
    override func checkRotated () {
        super.checkRotated()
        print ("Table Rotated")
        mTableView.frame = CGRect(x: 0, y: (viewHeader?.h)!, w: self.view.w, h: self.view.h - (viewHeader?.h)!)
        mTableView.reloadData()
        setup()
    }
}

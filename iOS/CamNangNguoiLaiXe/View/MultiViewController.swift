//
//  MultiViewController.swift
//  CamNangNguoiLaiXe
//
//  Created by Hung Le on 3/12/17.
//  Copyright © 2017 Hung Le. All rights reserved.
//

import UIKit

class MultiViewController: BaseViewController, UITableViewDelegate, UITableViewDataSource {
    
    var mTableView: UITableView = {
        let table = UITableView(frame: CGRect.zero, style: .plain)
        table.showsVerticalScrollIndicator = false
        table.separatorStyle = .none
//        table.backgroundColor = .clear
        table.backgroundColor = .white
        return table
    }()
    
    var model: smallTopic?
    
    override func viewDidLoad() {
        page = "MultiView"
        titlePage = model?.title
        super.viewDidLoad()
        checkRotated()
//        view.backgroundColor = UIColor(hexString: "E6E6E6")
        view.backgroundColor = .white
        print ("Scene : MultiView")

        btnBack?.isHidden = false
        btnTop?.isHidden = false
        updateUI()
//        setup()
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
        mTableView.register(UITableViewCell.self, forCellReuseIdentifier: "cell")
        mTableView.contentInset = UIEdgeInsetsMake(-10, 0, 0, 0)
        mTableView.delegate = self
        mTableView.dataSource = self
        view.addSubview(mTableView)
        setupAds()
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        if let small = model?.content {
            return small.count
        } else {
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 44
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        if section == 0{
            return 20
        } else {
            return 10
        }
    }
    
    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        if let small = model?.content {
            if section == small.count - 1 {
                return 50
            } else {
                return 0
            }
        } else {
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        var h: CGFloat = 0
        if section == 0{
            h = 20
        } else {
            h = 10
        }
        let v = UIView(frame: CGRect(x: 0, y: 0, w: tableView.w, h: h))
        if section == 0 {
//            v.backgroundColor = .clear
            v.backgroundColor = .white
        } else {
//            v.backgroundColor = UIColor(hexString: "E6E6E6")
            v.backgroundColor = .white
        }
        
        return v
    }
    
    func tableView(_ tableView: UITableView, viewForFooterInSection section: Int) -> UIView? {
        if let small = model?.content {
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
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath)
        let imgView = UIImageView(frame: CGRect(x: 0, y: 0, w: 10, h: 15))
        imgView.centerY = cell.h / 2
        imgView.image = #imageLiteral(resourceName: "small_arrow_right")
        cell.accessoryView = imgView
//        cell.backgroundColor = .clear
        cell.backgroundColor = .white
        cell.selectionStyle = .none
//        cell.textLabel?.textColor = UIColor(hexString: "A4A4A4")
        cell.textLabel?.textColor = .black
        cell.textLabel?.numberOfLines = 0
        cell.textLabel?.font = UIFont.systemFont(ofSize: 16)
        cell.imageView?.image = #imageLiteral(resourceName: "icon_document_gray")
        cell.imageView?.transform = CGAffineTransform(scaleX: 0.1, y: 0.1)
        let item = model?.content?[indexPath.section]
        cell.textLabel?.text = item?.title.uppercased()
        
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        let item = model?.content?[indexPath.section]
        let motVC = TextViewController()
        motVC.item = item
        self.navigationController?.pushViewController(motVC, animated: true)
    }
    
    override func checkRotated () {
        super.checkRotated()
        print ("Table Rotated")
    }
    
    override func onAfterLoadHeader () {
        btnBack?.isHidden = false
        btnTop?.isHidden = false
    }
    
    override func renderLandscape () {
        mTableView.frame = CGRect(x: 0, y: (viewHeader?.h)!, w: self.view.w, h: self.view.h - (viewHeader?.h)!)
        mTableView.reloadData()
        setup ()
    }
    
    override func renderPortrait () {
        mTableView.frame = CGRect(x: 0, y: (viewHeader?.h)!, w: self.view.w, h: self.view.h - (viewHeader?.h)!)
        mTableView.reloadData()
        setup ()
    }
    
}
